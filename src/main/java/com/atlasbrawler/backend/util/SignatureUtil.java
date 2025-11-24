package com.atlasbrawler.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class SignatureUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);
    private static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    
    public boolean verifySignature(String message, String signature, String expectedAddress) {
        try {
            String messageHash = Numeric.toHexString(getEthereumMessageHash(message));
            String recoveredAddress = recoverAddress(messageHash, signature);
            
            return recoveredAddress != null && 
                   recoveredAddress.equalsIgnoreCase(expectedAddress);
        } catch (Exception e) {
            logger.error("Error verifying signature", e);
            return false;
        }
    }
    
    public String recoverAddress(String messageHash, String signature) {
        try {
            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            byte v = signatureBytes[64];
            if (v < 27) {
                v += 27;
            }
            
            Sign.SignatureData signatureData = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64)
            );
            
            int header = 0;
            for (byte b : signatureData.getR()) {
                header = (header << 8) + (b & 0xFF);
            }
            
            byte vByte = signatureData.getV()[0];
            byte recId = (byte) (vByte - 27);
            BigInteger key = Sign.recoverFromSignature(
                recId,
                new ECDSASignature(
                    new BigInteger(1, signatureData.getR()),
                    new BigInteger(1, signatureData.getS())
                ),
                Numeric.hexStringToByteArray(messageHash)
            );
            
            if (key == null) {
                return null;
            }
            
            return "0x" + Keys.getAddress(key);
        } catch (Exception e) {
            logger.error("Error recovering address from signature", e);
            return null;
        }
    }
    
    public byte[] getEthereumMessageHash(String message) {
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        String prefixedMessage = prefix + message;
        return Hash.sha3(prefixedMessage.getBytes(StandardCharsets.UTF_8));
    }
    
    public boolean isValidAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        
        if (!address.matches("^0x[0-9a-fA-F]{40}$")) {
            return false;
        }
        
        return true;
    }
}
