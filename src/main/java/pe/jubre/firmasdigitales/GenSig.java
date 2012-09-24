package pe.jubre.firmasdigitales;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class GenSig {

	public static void main(String[] args) {
		/* Generate a DSA signature */
		try {
			KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

			keyGen.initialize(1024, random);

			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();

			Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");

			dsa.initSign(privateKey);

			FileInputStream fis = new FileInputStream("cv.pdf");
			BufferedInputStream bufin = new BufferedInputStream(fis);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bufin.read(buffer)) >= 0) {
				dsa.update(buffer, 0, len);
			}

			bufin.close();

			byte[] realSig = dsa.sign();

			/* save the signature in a file */
			FileOutputStream sigfos = new FileOutputStream("sig");
			sigfos.write(realSig);
			sigfos.close();

			/* save the public key in a file */
			byte[] key = publicKey.getEncoded();
			FileOutputStream keyfos = new FileOutputStream("suepk");
			keyfos.write(key);
			keyfos.close();

		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}

	}
}
