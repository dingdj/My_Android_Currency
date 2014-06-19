/**
 * @author dingdj
 * Date:2014-6-17上午11:44:38
 *
 */
package com.ddj.mycurrency.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.InflaterInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


import android.util.Base64;
import android.util.Log;

import com.ddj.commonkit.StringUtils;

/**
 * @author dingdj
 * Date:2014-6-17上午11:44:38
 *
 */
public class DeEncryptUtil {
	
	/**
	 * 解密
	 * @author dingdj
	 * Date:2014-6-18下午4:21:06
	 *  @param content
	 *  @return
	 */
	public static String deEncry(String content){
		Log.e("DeEncryptUtil", content);
		if(StringUtils.isNotEmpty(content)){
			content = content.replace((char) 0x2d, (char) 0x2b).replace((char)0x5f, (char)0x2f);
			byte[] b_content = Base64.decode(content.getBytes(), Base64.DEFAULT);
			byte[] b_key = Constant.KEY.getBytes();
			SecretKeySpec sks = new SecretKeySpec(b_key, "AES");
			try {
				Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
				cipher.init(Cipher.DECRYPT_MODE, sks);
				content = new String(cipher.doFinal(b_content));
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int delimiter = content.indexOf('|');
			String tmpstr = content.substring(0, delimiter);
			int length = Integer.valueOf(tmpstr);
			content = content.substring(delimiter+1, delimiter+1+length);
			StringBuilder output = new StringBuilder();
			byte[] rtn = Base64.decode(content, Base64.DEFAULT);
			InflaterInputStream s = new InflaterInputStream(new ByteArrayInputStream(rtn));
			InputStreamReader reader = new InputStreamReader(s);
			BufferedReader in = new BufferedReader(reader);
			String read;
			try{
				while ((read = in.readLine()) != null) {
					output.append(read);
				}
				return output.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}
	

}
