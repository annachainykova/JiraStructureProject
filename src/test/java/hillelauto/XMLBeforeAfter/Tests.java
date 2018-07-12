package hillelauto.XMLBeforeAfter;

import org.testng.annotations.*;

public class Tests {

    @BeforeSuite
    void bfSuiteSanity() {
        System.out.println("It's before suite before sanity group");
    }

    @BeforeTest
    void bfTestSanity() {
        System.out.println("It's before test before sanity group");
    }

    @BeforeClass
    void bfClassSanity() {
        System.out.println("It's before class before sanity group");
    }

    @BeforeGroups(groups = "Sanity")
    void bfGrSanity() {
        System.out.println("It's before groups before sanity group");
    }


    @BeforeMethod
    void bfMethodSanity() {
        System.out.println("It's before method before sanity group");
    }

    @Test(groups = {"Sanity"})
    void testSanity() {
        System.out.println("It's Sanity test ");
    }

    @Test(groups = {"Sanity"})
    void testSanity1() {
        System.out.println("It's Sanity1 test ");
    }

    @Test(groups = {"Sanity"})
    void testSanity2() {
        System.out.println("It's Sanity2 test ");
    }

    @Test(groups = {"Sanity"})
    void testSanity3() {
        System.out.println("It's Sanity3 test ");
    }

    @Test(groups = {"Other"})
    void testOther() {
        System.out.println("It's Other test ");
    }

    @Test(groups = {"Other"})
    void testOther1() {
        System.out.println("It's Other1 test ");
    }

    @Test(groups = {"Other"})
    void testOther3() {
        System.out.println("It's Other3 test ");
    }
}
