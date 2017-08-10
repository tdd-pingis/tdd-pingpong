/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities.tmc;

/**
 *
 * @author authority
 */
//Identical to the Status used by TMC Sandbox results
public enum ResultStatus {
    PASSED,
    TESTS_FAILED,
    COMPILE_FAILED,
    TESTRUN_INTERRUPTED,
    GENERIC_ERROR
}
