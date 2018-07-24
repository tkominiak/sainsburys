package sainsburys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Calculation of VAT from the gross")
class TotalTest {

	@ParameterizedTest
    @MethodSource("getGrossAndVat")
    public void testCalculationVatFromGross(Double grossInput, Double vatExpected) {
    	assertEquals(vatExpected, new Total(grossInput).getVat());
    }

	@ParameterizedTest
    static Stream<Arguments> getGrossAndVat() {
    	return Stream.of(
    	        Arguments.of(0.0D, 0.0D),
    	        Arguments.of(5.0D, 0.83D),
    	        Arguments.of(39.5D, 6.58D),
    	        Arguments.of(null, null)
    	    );
    }
}
