package com.example.domain.ports;
import com.example.avro.MessageAvro;
import io.smallrye.mutiny.Uni;

public interface EventPort {

    Uni<Void> publish(MessageAvro messageAvro);
}
