package ch.schnes.smartlabel;

public interface Observer {
	void update(String topic, Object message);
}