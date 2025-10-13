CREATE TABLE public.binary_contents
(
    id           uuid         NOT NULL,
    created_at   timestamptz  NOT NULL,
    file_name    varchar(255) NOT NULL,
    "size"       bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    CONSTRAINT pk_id PRIMARY KEY (id)
);


CREATE TABLE public.users (
                              id uuid NOT NULL,
                              created_at timestamptz NOT NULL,
                              updated_at timestamptz NULL,
                              username varchar(50) NOT NULL,
                              email varchar(100) NOT NULL,
                              "password" varchar(60) NOT NULL,
                              profile_id uuid NOT NULL,
                              CONSTRAINT username UNIQUE (username),
                              CONSTRAINT users_pk PRIMARY KEY (id),
                              CONSTRAINT email UNIQUE (email),
                              CONSTRAINT users_binary_contents_fk FOREIGN KEY (profile_id) REFERENCES public.binary_contents(id) ON DELETE SET NULL
);


CREATE TABLE public.user_statuses (
                                       id uuid NOT NULL,
                                       created_at timestamptz NOT NULL,
                                       updated_at timestamptz NULL,
                                       user_id uuid NOT NULL,
                                       last_active_at timestamptz NOT NULL,
                                       CONSTRAINT user_statuses_pk PRIMARY KEY (id),
                                       CONSTRAINT user_statuses_unique UNIQUE (user_id),
                                       CONSTRAINT user_statuses_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);


CREATE TABLE public.channels (
                                 id uuid NOT NULL,
                                 created_at timestamptz NOT NULL,
                                 updated_at timestamptz NULL,
                                 "name" varchar(100) NULL,
                                 description varchar(200) NULL,
                                 "type" varchar(10) NOT NULL CHECK ("type"  IN ('PUBLIC', 'PRIVATE')),
                                 CONSTRAINT channels_pk PRIMARY KEY (id)
);


CREATE TABLE public.read_statuses (
                                      id uuid NOT NULL,
                                      created_at timestamptz NOT NULL,
                                      updated_at timestamptz NULL,
                                      user_id uuid NOT NULL,
                                      channel_id uuid NOT NULL,
                                      last_read_at timestamptz NOT NULL,
                                      CONSTRAINT read_statuses_pk PRIMARY KEY (id),
                                      CONSTRAINT read_statuses_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
                                      CONSTRAINT read_statuses_channels_fk FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE
);



CREATE TABLE public.messages (
                                 id uuid NOT NULL,
                                 created_at timestamptz NOT NULL,
                                 updated_at timestamptz NULL,
                                 "content" text NULL,
                                 channel_id uuid NOT NULL,
                                 author_id uuid NOT NULL,
                                 CONSTRAINT messages_pk PRIMARY KEY (id),
                                 CONSTRAINT messages_channels_fk FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE,
                                 CONSTRAINT messages_users_fk FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE SET NULL
);


CREATE TABLE public.message_attatchments (
                                             message_id uuid NOT NULL,
                                             attatchment_id uuid NOT NULL,
                                             id uuid NOT NULL,
                                             created_at timestamptz NOT NULL,
                                             CONSTRAINT message_attatchments_pk PRIMARY KEY (id),
                                             CONSTRAINT message_attatchments_binary_contents_fk FOREIGN KEY (attatchment_id) REFERENCES public.binary_contents(id) ON DELETE CASCADE,
                                             CONSTRAINT message_attatchments_messages_fk FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE
);
