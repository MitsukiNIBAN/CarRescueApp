package com.kuaijie.carrescue.util.socket.code;



import android.util.Log;

import com.kuaijie.carrescue.util.socket.message.Head;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		Log.i("encode","开始编码==>" + msg);
		if (msg == null || msg.getHead() == null)
			throw  new Exception("The encode message is null");
		Head head = msg.getHead();
		out.writeByte(head.getHead());
		out.writeShort(msg.getLength());
		out.writeByte(head.getLength());
		if (head.getUrl() != null)
			out.writeBytes(head.getUrl());
		out.writeByte(msg.getChannel());
		out.writeByte(msg.getDirection());
		out.writeInt(msg.getSource());
		out.writeInt(msg.getDest());
		out.writeByte(msg.getCmdType());
		out.writeByte(msg.getOpStatus());	
		if (msg.getOptionData() != null)
			out.writeBytes(msg.getOptionData());
		out.writeShort(msg.getCheckSum());
		Log.i("encode","编码完成");
	}
}
