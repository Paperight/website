package com.paperight.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class PdfEncryptor {
	
	public static void encrypt(String filename, String outputFilename) throws Exception {
		AccessPermission accessPermission = new AccessPermission();
		accessPermission.setCanAssembleDocument(true);
		accessPermission.setCanExtractContent(false);
		accessPermission.setCanExtractForAccessibility(false);
		accessPermission.setCanFillInForm(false);
		accessPermission.setCanModify(true);
		accessPermission.setCanModifyAnnotations(false);
		PDDocument document = PDDocument.load( filename );
		if( !document.isEncrypted() ) {
			StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(generatedOwnerPassword(), "", accessPermission);
			standardProtectionPolicy.setEncryptionKeyLength(40);
            document.protect(standardProtectionPolicy);
		}
		document.save(outputFilename);
	}

	public static void encrypt(String filename) throws Exception {
		encrypt(filename, filename);
	}
	
	public static String generatedOwnerPassword() {
		PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		String password = "" + DateTime.now().getMillis(); 
		return passwordEncoder.encode(password);
	}
	
}
