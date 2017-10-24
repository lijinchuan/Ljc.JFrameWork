package Entity;

public class BlogType extends LJC.JFrameWork.Data.Mongo.MongoDocumentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DBName = "BlogDB";
	public static final String CollectionName = "blog.blogtype";

	private String blogTypeName;
	private boolean isValid;

	public String getBlogTypeName() {
		return this.blogTypeName;
	}

	public void setBlogTypeName(String value) {
		this.blogTypeName = value;
	}

	public boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(boolean value) {
		this.isValid = value;
	}
}
