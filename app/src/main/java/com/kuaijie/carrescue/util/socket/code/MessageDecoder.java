package com.kuaijie.carrescue.util.socket.code;


import android.util.Log;

import com.kuaijie.carrescue.util.socket.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

	public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		Log.i("decoder", "开始解码！");
		Object msg = super.decode(ctx, in);
		if(null != msg){
			Message message = Message.messageDecode((ByteBuf)msg);
		   if(message!=null){
			   Log.i("decoder","解码成功！");
			   return message;
		   }else{
			   Log.i("decoder","解码失败并关闭连接！");
			   ctx.channel().close();
		   }
		}
		return null;
	}

	

}
