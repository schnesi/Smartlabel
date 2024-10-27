package ch.schnes.smartlabel;

public interface Observer {
	void update(String topic, String message);
	void update(String message);
}