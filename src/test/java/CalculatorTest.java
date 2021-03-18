import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CalculatorTest {

    final double DELTA = 0.0001;

    private final Calculator calc = new Calculator();

    //Tests for add method
    @Test
    public void positiveSumTest() {
        double actualSum = calc.add(2.5555, 2.44445);
        double expectedSum = 5.0;
        Assert.assertEquals(actualSum, expectedSum, DELTA);
    }

    @Test
    public void infinitySumTest() {
        double actualSum = calc.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double expectedSum = Double.POSITIVE_INFINITY;
        Assert.assertEquals(actualSum, expectedSum);
    }

    @Test
    public void positiveAndNegativeInfinitySumTest() {
        double actualSum = calc.add(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Assert.assertTrue(Double.isNaN(actualSum));
    }

    @Test
    public void positiveNegativeInfinitySumTest() {
        double actualSum = calc.add(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Assert.assertTrue(Double.isNaN(actualSum));
    }

    //Tests for multiply method
    @Test
    public void multiplyInfinityByZeroTest() {
        double actualProduct = calc.multiply(0, Double.POSITIVE_INFINITY);
        Assert.assertTrue(Double.isNaN(actualProduct));
    }

    @Test
    public void multiplyByItselfTest() {
        double actualProduct = calc.multiply(5, 5);
        Assert.assertTrue(actualProduct == 25);

    }

    @Test
    public void multiplyByOneTest() {
        double actualProduct = calc.multiply(1, 34234.6345);
        double expectedProduct = 34234.6345;
        Assert.assertEquals(actualProduct, expectedProduct);
    }

    @Test
    public void multiplyByZeroTest() {
        double actualProduct = calc.multiply(Double.MAX_VALUE, 0);
        Assert.assertEquals(actualProduct, 0);
    }


    @Test
    public void positiveProductTest() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(calc.multiply(34.434, 7688.88) > 0);
        softAssert.assertTrue(calc.multiply(-3473.4554, -797.99) > 0);
        softAssert.assertAll();
    }

    //Tests for subtract method
    @Test
    public void subtractItselfTest() {
        double actualDiff = calc.subtract(34.555, 34.555);
        Assert.assertTrue(actualDiff == 0);
    }

    @Test
    public void subtractTest() {
        double actualDiff = calc.subtract(44.7777, 40.7777);
        Assert.assertEquals(actualDiff, 4);
    }

    @Test
    public void subtractMinFromMaxTest() {
        double actualDiff = calc.subtract(35.4444, 3432.32427);
        Assert.assertTrue(actualDiff < 0);
    }

    @Test
    public void subtractFromMaxMinTest() {
        double actualDiff = calc.subtract(3453.34234, 2222.3652364);
        Assert.assertTrue(actualDiff > 0);
    }

    //Tests for divide method
    @Test
    public void divideByZeroTest() throws Exception {
        calc.divide(25.76586, 0);
    }

    @Test
    public void divideByInfinityTest() {
        double actualQuotient = calc.divide(34234, Double.POSITIVE_INFINITY);
        Assert.assertEquals(actualQuotient, 0, DELTA);
    }

    @Test
    public void infinityDivideByInfinityTest() {
        double actualQuotient = calc.divide(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Assert.assertTrue(Double.isNaN(actualQuotient));
    }

    @Test
    public void positiveQuotientTest() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(calc.divide(4253.543, 4233.666) > 0);
        softAssert.assertTrue(calc.divide(-4253.543, -4233.666) > 0);
    }
}
