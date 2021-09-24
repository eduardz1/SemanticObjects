package no.uio.microobject.test.type

import kotlin.test.assertFalse

class SMOLTypeTest : MicroObjectTypeTest() {
    init{
        "Query check success 1"{
            val tC = checkMet("Test", "mSuccess1", "type_query")
            assert(tC.report(false))
        }
        "Query check success 2"{
            val tC = checkMet("Test", "mSuccess2", "type_query")
            assert(tC.report(false))
        }
        "Query check success 3"{
            val tC = checkMet("Test", "mSuccess3", "type_query")
            assert(tC.report(false))
        }

        "Query check fail 1"{
            val tC = checkMet("Test", "mFail1", "type_query")
            assertFalse(tC.report(false))
        }

        "Query check fail 2"{
            val tC = checkMet("Test", "mFail2", "type_query")
            assertFalse(tC.report(false))
        }

        "Query check fail 3"{
            val tC = checkMet("Test", "mFail3", "type_query")
            assert(tC.report(false)) //%parameters are a warning now
        }
        "Query rule success"{
            val tC = checkMet("F", "getI", "type_query")
            assert(tC.report(false))
        }

        "Query rule fail"{
            val tC = checkMet("F", "errorGet", "type_query")
            assertFalse(tC.report(false))
        }
    }
}
