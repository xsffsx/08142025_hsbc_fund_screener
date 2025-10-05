package amendments.product

import spock.lang.Specification

/**
 * Test product amendment.
 */
class CreateUpdateTests extends Specification {
    /**
     * Test update a product and then approve it
     *
     * Steps:
     *     1. amendmentCreate(update)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. productByFilter to find the updated product, expect revision + 1, fields changed
     */
    def 'approve happy flow'() {
    }

    /**
     * Test update a product and then reject it
     *
     * Steps:
     *     1. amendmentCreate(update)
     *     2. amendmentRequestApproval with the amendmentId generated in (1)
     *     3. amendmentApprove(returned) with the amendmentId generated in (1)
     *     4. amendmentUpdate with the amendmentId generated in (1)
     *     5. amendmentRequestApproval with the amendmentId generated in (1)
     *     6. amendmentApprove(approved) with the amendmentId generated in (1)
     *     7. productByFilter to find the updated product, expect revision did not changed, fields did not changed
     */
    def 'reject happy flow'() {
    }
}

