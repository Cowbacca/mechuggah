package uk.ac.rhul.cs.zwac076.mechuggah.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class EventManagerTest {

	private static final Event TEST_EVENT = new Event();
	private EventManager eventManager;
	@Mock
	private EventListener mockListener;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		eventManager = EventManager.getInstance();
	}

	@Test
	public void testGetInstance() {
		assertEquals(EventManager.class, EventManager.getInstance().getClass());
	}

	@Test
	public void testGetInstanceTwice() {
		assertEquals(EventManager.getInstance(), EventManager.getInstance());
	}

	@Test
	public void testPublishEvent() {
		eventManager.registerListener(Event.class, mockListener);
		eventManager.publishEvent(TEST_EVENT);
		verify(mockListener).handle(TEST_EVENT);
	}
}
