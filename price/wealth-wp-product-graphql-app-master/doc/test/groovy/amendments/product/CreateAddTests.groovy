package amendments.product

import spock.lang.Specification

/**
 * Test product amendment.
 */
class CreateAddTests extends Specification {
    /**
     * Test create a new product and then approve it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. productByFilter to find the new created product, expect found
     */
    def 'approved happy flow'() {
    }

    /**
     * Test create a new product and then reject it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(rejected) with the amendmentId generated in (1)
     *     7. productByFilter to find the new created product, expect not found
     */
    def 'rejected happy flow'() {
    }
}

