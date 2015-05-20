package uk.me.mantas.eternity.serializer.write;

import java.io.IOException;

public class ValueWriteCommand extends WriteCommand {
	public Object data;
	public ValueWriteCommand (Object data) {
		this.data = data;
	}

	@Override
	public void write (BinaryWriter writer) throws IOException {
		writer.writeValue(data);
	}
}
