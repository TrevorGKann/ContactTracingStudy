package com.trevor.CoCoT;

import android.os.Build;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FingerprintGenerator {

    static final int FINGERPRINT_MAX = 4;
    static final String prefix = "dozmarbinwansamlitsighidfidlissogdirwacsabwissib"+
            "rigsoldopmodfoglidhopdardorlorhodfolrintogsilmir"+
            "holpaslacrovlivdalsatlibtabhanticpidtorbolfosdot"+
            "losdilforpilramtirwintadbicdifrocwidbisdasmidlop"+
            "rilnardapmolsanlocnovsitnidtipsicropwitnatpanmin"+
            "ritpodmottamtolsavposnapnopsomfinfonbanmorworsip"+
            "ronnorbotwicsocwatdolmagpicdavbidbaltimtasmallig"+
            "sivtagpadsaldivdactansidfabtarmonranniswolmispal"+
            "lasdismaprabtobrollatlonnodnavfignomnibpagsopral"+
            "bilhaddocridmocpacravripfaltodtiltinhapmicfanpat"+
            "taclabmogsimsonpinlomrictapfirhasbosbatpochactid"+
            "havsaplindibhosdabbitbarracparloddosbortochilmac"+
            "tomdigfilfasmithobharmighinradmashalraglagfadtop"+
            "mophabnilnosmilfopfamdatnoldinhatnacrisfotribhoc"+
            "nimlarfitwalrapsarnalmoslandondanladdovrivbacpol"+
            "laptalpitnambonrostonfodponsovnocsorlavmatmipfip";

    static final String sufix = "zodnecbudwessevpersutletfulpensytdurwepserwylsun"+
            "rypsyxdyrnuphebpeglupdepdysputlughecryttyvsydnex"+
            "lunmeplutseppesdelsulpedtemledtulmetwenbynhexfeb"+
            "pyldulhetmevruttylwydtepbesdexsefwycburderneppur"+
            "rysrebdennutsubpetrulsynregtydsupsemwynrecmegnet"+
            "secmulnymtevwebsummutnyxrextebfushepbenmuswyxsym"+
            "selrucdecwexsyrwetdylmynmesdetbetbeltuxtugmyrpel"+
            "syptermebsetdutdegtexsurfeltudnuxruxrenwytnubmed"+
            "lytdusnebrumtynseglyxpunresredfunrevrefmectedrus"+
            "bexlebduxrynnumpyxrygryxfeptyrtustyclegnemfermer"+
            "tenlusnussyltecmexpubrymtucfyllepdebbermughuttun"+
            "bylsudpemdevlurdefbusbeprunmelpexdytbyttyplevmyl"+
            "wedducfurfexnulluclennerlexrupnedlecrydlydfenwel"+
            "nydhusrelrudneshesfetdesretdunlernyrsebhulryllud"+
            "remlysfynwerrycsugnysnyllyndyndemluxfedsedbecmun"+
            "lyrtesmudnytbyrsenwegfyrmurtelreptegpecnelnevfes";

    public static String getTriple(byte input, boolean isPrefix){
        int offset = Byte.toUnsignedInt(input);
        int start = offset * 3;
        int stop = start + 3;
        if (isPrefix){
            return prefix.substring(start, stop);
        } else {
            return sufix.substring(start, stop);
        }
    }

    public static String getFingerPrint(){
        byte[] bytes = getFingerPrintBytes();
        if (bytes == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length && i < FINGERPRINT_MAX; i++){
            boolean isPrefix = i % 2 == 0;
            sb.append(getTriple(bytes[i], isPrefix));
            if (!isPrefix && i < bytes.length - 1 && i < FINGERPRINT_MAX - 1){
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public static byte[] getFingerPrintBytes(){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Build.FINGERPRINT.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
