package marketplace.domain.builders

import marketplace.Constants


enum ApprovalStatus {

    IN_PROGRESS(Constants.APPROVAL_STATUSES['IN_PROGRESS']),
    PENDING(Constants.APPROVAL_STATUSES['PENDING']),
    APPROVED(Constants.APPROVAL_STATUSES['APPROVED']),
    REJECTED(Constants.APPROVAL_STATUSES['REJECTED'])

    final String status

    ApprovalStatus(String status) {
        this.status = status
    }

}
