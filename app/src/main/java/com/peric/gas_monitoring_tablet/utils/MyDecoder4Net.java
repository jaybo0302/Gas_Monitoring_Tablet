package com.peric.gas_monitoring_tablet.utils;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyDecoder4Net implements ProtocolDecoder {
	private Charset charset = Charset.forName("UTF-8");
	IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		int count = 0; 
		while (in.hasRemaining()) {
				count++;
	            byte b = in.get();
	            buf.put(b);
	            //目前收到很多null去看看是不是socket关的太快了。。。去拿一下inputstream试试
	            if (count == 29) {
	                buf.flip();
	                byte[] msg = new byte[buf.limit()];
	                buf.get(msg);
	                String message = hex2String(msg, msg.length);
	                //解码成功，把buf重置
	                buf = IoBuffer.allocate(100).setAutoExpand(true);
	                out.write(message);
	            }
	        }
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
		
	}
	
	public static String hex2String(byte[] b, int length) {
    	StringBuffer sb = new StringBuffer();
    	for(int i = 0; i < length; i++) {
    		String hex = Integer.toHexString(0xFF & b[i]);
    		if(hex.length() == 1) {
    			sb.append("0");
    		}
    		sb.append(hex);
    		sb.append(" ");
    	}
    	String s = sb.toString();
    	return s;
    }
}

