package kore.botssdk.fileupload.models;

public class ChunkInfo {

	
	private int id;
	
	private int number;
	
	private int size;

	private int offset;

	private boolean isUploaded;

	private String fileName;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isUploaded() {
		return isUploaded;
	}

	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
