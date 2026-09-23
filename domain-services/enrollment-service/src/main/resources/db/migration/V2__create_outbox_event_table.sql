CREATE TABLE tb_outbox_event (
                                 id UUID PRIMARY KEY,
                                 aggregate_type VARCHAR(100) NOT NULL,
                                 aggregate_id UUID NOT NULL,
                                 event_type VARCHAR(100) NOT NULL,
                                 exchange_name VARCHAR(100) NOT NULL,
                                 routing_key VARCHAR(100) NOT NULL,
                                 payload TEXT NOT NULL,
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                 published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_event_published_at ON tb_outbox_event(published_at);