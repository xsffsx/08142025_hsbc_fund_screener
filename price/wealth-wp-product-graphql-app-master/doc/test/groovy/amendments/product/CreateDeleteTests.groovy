package amendments.product

import spock.lang.Specification

/**
 * Test product amendment.
 */
class CreateDeleteTests extends Specification {
    /**
     * Test delete a product and then approve it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentRequestApproval with the amendmentId generated in (1)
     *     4. amendmentApprove(approved) with the amendmentId generated in (1)
     *     5. amendmentCreate(delete)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. productByFilter to find the deleted item in product, expect found with prodStatCde=='D'
     */
    def 'approved happy flow'() {
    }

    /**
     * Test delete a product and then reject it
     *
     * Steps:
     *     1. amendmentCreate(add)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentRequestApproval with the amendmentId generated in (1)
     *     4. amendmentApprove(approved) with the amendmentId generated in (1)
     *     5. amendmentCreate(delete)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. productByFilter to find the deleted item in product, expect found with prodStatCde!='D'
     */
    def 'reject happy flow'() {
    }
}

