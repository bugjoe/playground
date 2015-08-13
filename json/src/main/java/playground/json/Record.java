package playground.json;

public class Record {
    private String name;
    private String type;
    private String clazz;
    private String data;
    private int ttl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

	public void throwException() {
		throw new NullPointerException("Am I drunk?!");
	}

    @Override
    public String toString() {
        return "[name:" + name + ", type:" + type + ", clazz:" + clazz + ", data:" + data + ", ttl:" + ttl + "]";
    }
}
