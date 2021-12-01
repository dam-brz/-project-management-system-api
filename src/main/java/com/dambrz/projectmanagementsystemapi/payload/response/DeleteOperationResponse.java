package com.dambrz.projectmanagementsystemapi.payload.response;

public class DeleteOperationResponse {

    private final boolean success;

    public DeleteOperationResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
