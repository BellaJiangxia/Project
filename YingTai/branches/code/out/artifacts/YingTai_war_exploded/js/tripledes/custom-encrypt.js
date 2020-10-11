function encryptByDES(message, key) {
	key = "" + key + key;
	var keyHex = CryptoJS.enc.Utf8.parse(key.toLowerCase());
	var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});
	return encrypted.toString();
}

function decryptByDES(ciphertext, key) {
	key = "" + key + key;
	var keyHex = CryptoJS.enc.Utf8.parse(key);

	var decrypted = CryptoJS.DES.decrypt({
		ciphertext : CryptoJS.enc.Base64.parse(ciphertext)
	}, keyHex, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});

	return decrypted.toString(CryptoJS.enc.Utf8);
}