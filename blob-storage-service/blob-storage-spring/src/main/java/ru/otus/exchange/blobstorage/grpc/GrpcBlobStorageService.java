package ru.otus.exchange.blobstorage.grpc;

import io.grpc.stub.StreamObserver;
import ru.otus.exchange.blobstorage.Storage;
import ru.otus.exchange.blobstorage.api.grpc.v1.BlobStorageApiV1;
import ru.otus.exchange.blobstorage.api.grpc.v1.BlobStorageServiceGrpc;

@SuppressWarnings({"java:S1185", "java:S1068"})
public class GrpcBlobStorageService extends BlobStorageServiceGrpc.BlobStorageServiceImplBase {

    private final Storage storage;

    public GrpcBlobStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void getObject(
            BlobStorageApiV1.StorageKey request, StreamObserver<BlobStorageApiV1.StorageData> responseObserver) {
        super.getObject(request, responseObserver);
    }

    @Override
    public void putObject(
            BlobStorageApiV1.StorageData request, StreamObserver<BlobStorageApiV1.OpResult> responseObserver) {
        super.putObject(request, responseObserver);
    }

    @Override
    public void removeObject(
            BlobStorageApiV1.StorageKey request, StreamObserver<BlobStorageApiV1.OpResult> responseObserver) {
        super.removeObject(request, responseObserver);
    }

    @Override
    public void isExists(
            BlobStorageApiV1.StorageKey request, StreamObserver<BlobStorageApiV1.OpResult> responseObserver) {
        super.isExists(request, responseObserver);
    }

    @Override
    public void removeObjects(
            BlobStorageApiV1.Exchange request, StreamObserver<BlobStorageApiV1.OpResult> responseObserver) {
        super.removeObjects(request, responseObserver);
    }
}
