package jay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parser_validEvent_returnsEvent() throws JayException {
        Event e = Parser.parseEvent("meeting /from 2025-09-01 0900 /to 2025-09-01 1030");
        assertEquals("[E][ ] meeting (from: Sep 01 2025 9:00am to: Sep 01 2025 10:30am)", e.toString());
    }

    @Test
    public void parser_invalidEvent_exceptionThrown() {
        assertThrows(JayException.class, () -> Parser.parseEvent("party /from 2025-09-01 0900"));
        assertThrows(JayException.class, () -> Parser.parseEvent("party /to 2025-09-01 0900"));
    }

    @Test
    public void parser_invalidDateTime_exceptionThrown() {
        assertThrows(JayException.class, () -> Parser.parseEvent("lecture /from 2025-09-01 0900 /to not-a-date"));
    }
}
