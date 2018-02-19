
package com.kuaijie.carrescue.util.socket.message;

public enum MessageType {
	
    ShakeHand((byte) 0x00), 
    Login((byte) 0x01), 
    Other((byte) 0x02), 
	HEARTBEAT((byte) 0x03),
	
	Exception ((byte) 0x11),
	Error ((byte) 0x00),
	Success ((byte) 0x01);

	private byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}
