package com.example.account.config.encoder;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

public class CustomPasswordEncoder implements PasswordEncoder {	

	
	@Override
	public String encode(CharSequence rawPassword) {
		try {
			return createHash(rawPassword.toString());
		} catch (CustomException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	try {
			return validatePassword(rawPassword.toString(), encodedPassword);
		} catch (CustomException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static String[] simpleList = {
			"password",
			"password1",
			"drowssap",
			"123456",
			"654321",
			"abcdefg",
			"gfedcba",
			"111111",
			"bbbbbb",
			"123123",
			"007007",
			"hahaha",
			"21122112",
			"qwerty",
			"qwertyui",
			"qwerty1",
			"qwerty12",
			"asdfgh",
			"asdfghjk",
			"monkey1",
			"donkey1"
	};
	private static String[] blackList = {
			"abc123",
			"monkey",
			"letmein",
			"dragon",
			"baseball",
			"iloveyou",
			"trustno1",
			"sunshine",
			"master",
			"welcome",
			"shadow",
			"ashley",
			"football",
			"jesus",
			"michael",
			"ninja",
			"mustang",
			"pussy",
			"baseball",
			"jennifer",
			"jordan",
			"superman",
			"harley",
			"fuckme",
			"hunter",
			"fuckyou",
			"ranger",
			"buster",
			"thomas",
			"tigger",
			"robert",
			"soccer",
			"fuck",
			"batman",
			"test",
			"pass",
			"killer",
			"hockey",
			"george",
			"charlie",
			"andrew",
			"michelle",
			"love",
			"jessica",
			"asshole",
			"pepper",
			"daniel",
			"access",
			"joshua",
			"maggie",
			"starwars",
			"silver",
			"william",
			"dallas",
			"yankees",
			"hello",
			"amanda",
			"orange",
			"biteme",
			"freedom",
			"computer",
			"sexy",
			"thunder",
			"nicole",
			"ginger",
			"heather",
			"summer",
			"corvette",
			"taylor",
			"fucker",
			"austin",
			"merlin",
			"matthew",
			"golfer",
			"cheese",
			"princess",
			"martin",
			"chelsea",
			"patrick",
			"richard",
			"diamond",
			"yellow",
			"bigdog",
			"secret",
			"sparky",
			"cowboy",
			"camaro",
			"anthony",
			"matrix",
			"falcon",
			"iloveyou",
			"iceman",
			"zxcvbn",
			"booboo",
			"ncc1701",
			"ncc1701d",
			"bigdick",
			"blowjob",
			"bigtits",
			"badboy",
			"blowme",
			"bigdaddy",
			"redsox",
			"thx1138",
			"asdf",
			"zxcvbnm",
			"qazwsx",
			"butthead",
			"iwantu",
			"hooters",
			"startrek",
			"maddog",
			"theman",
			"liverpoo",
			"redskins",
			"newyork",
			"bubba",
			"helpme",
			"pookie",
			"8675309",
			"suckit",
			"5150",
			"shithead",
			"fuckoff",
			"hotdog",
			"rosebud",
			"2112",
			"cocacola",
			"bond007",
			"rush2112",
			"red123",
			"ou812",
			"topgun",
			"bigboy",
			"lifehack",
			"heka6w2",
			"cumshot",
			"jordan23",
			"eagle",
			"bullshit",
			"therock",
			"dickhead",
			"redwings",
			"cartman",
			"test123",
			"hotrod",
			"alexande",
			"passw0rd",
			"bigcock",
			"lasvegas",
			"slipknot",
			"1q2w3e",
			"1q2w3e4r",
			"sandman",
			"azerty",
			"money",
			"loveme",
			"poohbear",
			"badass",
			"assman",
			"maryjane",
			"spitfire",
			"1qaz2wsx",
			"q1w2e3r4",
			"westside",
			"suckme",
			"semperfi",
			"redrum",
			"freeuser",
			"babygirl",
			"babydoll",
			"lucky",
			"hardon",
			"wolverine",
			"freepass",
			"bigred",
			"pearljam",
			"peekaboo",
			"budlight",
			"stargate",
			"bigman",
			"swordfis",
			"blink182",
			"rolltide",
			"happy",
			"poopoo",
			"eatme",
			"lickme",
			"spiderma",
			"goblue",
			"phpbb",
			"whynot",
			"wolfpack",
			"tiger",
			"madmax",
			"srinivas",
			"bigmac",
			"chris",
			"tarheels",
			"catch22",
			"159753",
			"alpha1",
			"hawkeye",
			"vegeta",
			"james",
			"billybob",
			"changeme",
			"guest",
			"guest123",
			"darkside",
			"wutang",
			"darkstar",
			"bigone",
			"bobafett",
			"transam",
			"openup",
			"fuckit",
			"xfiles",
			"manutd",
			"mnbvcxz",
			"redneck",
			"devildog",
			"angel",
			"access14",
			"buddy",
			"lonewolf",
			"theone",
			"basketba",
			"wetpussy",
			"foobar",
			"hotstuff",
			"rocky",
			"fuck_inside",
			"goforit",
			"abgrtyu",
			"wrinkle",
			"bigboobs",
			"bulls",
			"brent"
	};
	
	public static void validate(String password) throws CustomException {
		String msg = null;
		if (password == null || password == "") {
			msg = "Missing password";
		}
		if (password.length() < 6) {
			msg = "Minimum password length: 6 characters";
		}
		if (password.length() > 128) {
			msg = "Maximum password length: 128 characters";
		}
		for (String simple : simpleList) {
			if (simple.equalsIgnoreCase(password)) {
				msg = "The password is simple";
			}
		}
		for (String black : blackList) {
			if (black.equalsIgnoreCase(password)) {
				msg = "The password is in the blacklist";
			}
		}
		if (msg != null)
			throw new CustomException(APIStatus.BAD_REQUEST, msg);
	}
	
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              a salted PBKDF2 hash of the password
     */
    public static String createHash(String password) throws CustomException {
    	try {
			return createHash(password.toCharArray());
		} catch (Exception e) {
			throw new CustomException(APIStatus.RUNNING_TIME_ERROR, e.getMessage());
		} 
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     * @return              a salted PBKDF2 hash of the password
     */
    public static String createHash(char[] password) throws CustomException {
    	try {
	    	// Generate a random salt
	        SecureRandom random = new SecureRandom();
	        byte[] salt = new byte[SALT_BYTE_SIZE];
	        random.nextBytes(salt);
	
	        // Hash the password
	        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
	        // format iterations:salt:hash
	        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
    	} catch (Exception e) {
    		throw new CustomException(APIStatus.RUNNING_TIME_ERROR, e.getMessage());
    	}
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password        the password to check
     * @param   correctHash     the hash of the valid password
     * @return                  true if the password is correct, false if not
     */
    public static boolean validatePassword(String password, String correctHash) throws CustomException {
        try {
        	return validatePassword(password.toCharArray(), correctHash);
        } catch (Exception e) {
        	throw new CustomException(APIStatus.RUNNING_TIME_ERROR, e.getMessage());
        }
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password        the password to check
     * @param   correctHash     the hash of the valid password
     * @return                  true if the password is correct, false if not
     */
    public static boolean validatePassword(char[] password, String correctHash) throws CustomException {
    	try {
	    	// Decode the hash into its parameters
	        String[] params = correctHash.split(":");
	        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
	        byte[] salt = fromHex(params[SALT_INDEX]);
	        byte[] hash = fromHex(params[PBKDF2_INDEX]);
	        // Compute the hash of the provided password, using the same salt, 
	        // iteration count, and hash length
	        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
	        // Compare the hashes in constant time. The password is correct if
	        // both hashes match.
	        return slowEquals(hash, testHash);
    	} catch (Exception e) {
    		throw new CustomException(APIStatus.RUNNING_TIME_ERROR, e.getMessage());
    	}
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line 
     * system using a timing attack and then attacked off-line.
     * 
     * @param   a       the first byte array
     * @param   b       the second byte array 
     * @return          true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}
