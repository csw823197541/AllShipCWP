package listener;

import java.awt.event.ActionEvent;
import java.util.EventObject;

/**
 * Created by csw on 2016/12/14 15:00.
 * Explain:
 */
public class TableEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public TableEvent(Object source) {
        super(source);
    }
}
