package io.github.leoniedermeier.iex.example.refdata;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

public class RefDataControllerTest {

    private RefDataService refDataService;
    private RefDataController refDataController;

    private void check(final int fromIndex, final int expectedResultLength) {
        final ModelAndView modelAndView = this.refDataController.refdata(fromIndex);
        final List<RefDataSymbol> refDataSymbols = ModelAndViewAssert.assertAndReturnModelAttributeOfType(modelAndView,
                "refDataSymbols", List.class);
        assertThat(refDataSymbols, Matchers.iterableWithSize(expectedResultLength));
    }

    @Before
    public void init() {

        this.refDataService = Mockito.mock(RefDataService.class);
        this.refDataController = new RefDataController(this.refDataService);

        final List<RefDataSymbol> list = IntStream.range(10, 30)
                .mapToObj(i -> new RefDataSymbol(i + "-symbol", i + "-name")).collect(Collectors.toList());

        when(this.refDataService.getRefDataSymbols()).thenReturn(list);

    }

    @Test
    public void testRefData_char_to_index() {
        final ModelAndView modelAndView = this.refDataController.refdata(3);
        final List<RefDataController.CharAndIndex> charToIndex = ModelAndViewAssert
                .assertAndReturnModelAttributeOfType(modelAndView, "charToIndex", List.class);

        assertThat(charToIndex, Matchers.iterableWithSize(2));

        assertThat(charToIndex.get(0).getCharacter(), equalTo('1'));
        assertThat(charToIndex.get(0).getIndex(), equalTo(0));

        assertThat(charToIndex.get(1).getCharacter(), equalTo('2'));
        assertThat(charToIndex.get(1).getIndex(), equalTo(10));
    }

    @Test
    public void testRefData_with_parameters() {

        final ModelAndView modelAndView = this.refDataController.refdata(3);
        final List<RefDataSymbol> refDataSymbols = ModelAndViewAssert.assertAndReturnModelAttributeOfType(modelAndView,
                "refDataSymbols", List.class);

        assertThat(refDataSymbols, Matchers.iterableWithSize(15));
        assertThat(refDataSymbols.get(0).getName(), Matchers.equalTo("13-name"));
        assertThat(refDataSymbols.get(14).getSymbol(), Matchers.equalTo("27-symbol"));

    }

    @Test
    public void testRefData_with_parameters_from_index_lower_than_0() {

        final ModelAndView modelAndView = this.refDataController.refdata(-3);
        final List<RefDataSymbol> refDataSymbols = ModelAndViewAssert.assertAndReturnModelAttributeOfType(modelAndView,
                "refDataSymbols", List.class);
        assertThat(refDataSymbols, Matchers.iterableWithSize(15));
        assertThat(refDataSymbols.get(0).getName(), Matchers.equalTo("10-name"));
    }

    @Test
    public void testRefData_with_parameters_from_index_to_big() {
        check(20, 0);
    }

    @Test
    public void testRefData_with_parameters_from_length_to_big() {

        check(13, 7);
    }

}
