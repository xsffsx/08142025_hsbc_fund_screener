--
-- PostgreSQL database dump
--

-- Dumped from database version 13.21 (Debian 13.21-1.pgdg120+1)
-- Dumped by pg_dump version 13.21 (Debian 13.21-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: hstore; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: agent; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE agent (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    avatar character varying(500),
    status character varying(50) DEFAULT 'draft'::character varying,
    prompt text,
    category character varying(100),
    admin_id bigint,
    tags text,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: agent_datasource; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE agent_datasource (
    id bigint NOT NULL,
    agent_id bigint NOT NULL,
    datasource_id bigint NOT NULL,
    is_active integer DEFAULT 1,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: agent_datasource_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agent_datasource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: agent_datasource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agent_datasource_id_seq OWNED BY agent_datasource.id;


--
-- Name: agent_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agent_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: agent_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agent_id_seq OWNED BY agent.id;


--
-- Name: agent_knowledge; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE agent_knowledge (
    id bigint NOT NULL,
    agent_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    content text,
    type character varying(50) DEFAULT 'document'::character varying,
    category character varying(100),
    tags text,
    status character varying(50) DEFAULT 'active'::character varying,
    source_url character varying(500),
    file_path character varying(500),
    file_size bigint,
    file_type character varying(100),
    embedding_status character varying(50) DEFAULT 'pending'::character varying,
    creator_id bigint,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: agent_knowledge_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agent_knowledge_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: agent_knowledge_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agent_knowledge_id_seq OWNED BY agent_knowledge.id;


--
-- Name: agent_preset_question; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE agent_preset_question (
    id bigint NOT NULL,
    agent_id bigint NOT NULL,
    question text NOT NULL,
    sort_order integer DEFAULT 0,
    is_active integer DEFAULT 1,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: agent_preset_question_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agent_preset_question_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: agent_preset_question_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agent_preset_question_id_seq OWNED BY agent_preset_question.id;


--
-- Name: business_knowledge; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE business_knowledge (
    id bigint NOT NULL,
    business_term character varying(255) NOT NULL,
    description text,
    synonyms text,
    is_recall integer DEFAULT 1,
    data_set_id character varying(255),
    agent_id character varying(255),
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: business_knowledge_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE business_knowledge_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_knowledge_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE business_knowledge_id_seq OWNED BY business_knowledge.id;


--
-- Name: datasource; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE datasource (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    type character varying(50) NOT NULL,
    host character varying(255) NOT NULL,
    port integer NOT NULL,
    database_name character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    connection_url character varying(1000),
    status character varying(50) DEFAULT 'active'::character varying,
    test_status character varying(50) DEFAULT 'unknown'::character varying,
    description text,
    creator_id bigint,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: datasource_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE datasource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: datasource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE datasource_id_seq OWNED BY datasource.id;

--
-- Name: semantic_model; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE semantic_model (
    id bigint NOT NULL,
    agent_id character varying(255),
    field_name character varying(255) DEFAULT ''::character varying NOT NULL,
    synonyms text,
    origin_name character varying(255) DEFAULT ''::character varying,
    description text,
    origin_description character varying(255),
    type character varying(255) DEFAULT ''::character varying,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_recall integer DEFAULT 1,
    status integer DEFAULT 1
);


--
-- Name: semantic_model_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE semantic_model_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: semantic_model_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE semantic_model_id_seq OWNED BY semantic_model.id;

--
-- Name: agent id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent ALTER COLUMN id SET DEFAULT nextval('agent_id_seq'::regclass);


--
-- Name: agent_datasource id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_datasource ALTER COLUMN id SET DEFAULT nextval('agent_datasource_id_seq'::regclass);


--
-- Name: agent_knowledge id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_knowledge ALTER COLUMN id SET DEFAULT nextval('agent_knowledge_id_seq'::regclass);


--
-- Name: agent_preset_question id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_preset_question ALTER COLUMN id SET DEFAULT nextval('agent_preset_question_id_seq'::regclass);


--
-- Name: business_knowledge id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY business_knowledge ALTER COLUMN id SET DEFAULT nextval('business_knowledge_id_seq'::regclass);


--
-- Name: datasource id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY datasource ALTER COLUMN id SET DEFAULT nextval('datasource_id_seq'::regclass);


--
-- Name: document_embeddings id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY document_embeddings ALTER COLUMN id SET DEFAULT nextval('document_embeddings_id_seq'::regclass);


--
-- Name: employees id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY employees ALTER COLUMN id SET DEFAULT nextval('employees_id_seq'::regclass);


--
-- Name: query_stats id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY query_stats ALTER COLUMN id SET DEFAULT nextval('query_stats_id_seq'::regclass);


--
-- Name: semantic_model id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY semantic_model ALTER COLUMN id SET DEFAULT nextval('semantic_model_id_seq'::regclass);


--
-- Name: agent_datasource agent_datasource_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_datasource
    ADD CONSTRAINT agent_datasource_pkey PRIMARY KEY (id);


--
-- Name: agent_knowledge agent_knowledge_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_knowledge
    ADD CONSTRAINT agent_knowledge_pkey PRIMARY KEY (id);


--
-- Name: agent agent_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (id);


--
-- Name: agent_preset_question agent_preset_question_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY agent_preset_question
    ADD CONSTRAINT agent_preset_question_pkey PRIMARY KEY (id);


--
-- Name: business_knowledge business_knowledge_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY business_knowledge
    ADD CONSTRAINT business_knowledge_pkey PRIMARY KEY (id);


--
-- Name: datasource datasource_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY datasource
    ADD CONSTRAINT datasource_pkey PRIMARY KEY (id);


--
-- Name: document_embeddings document_embeddings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY document_embeddings
    ADD CONSTRAINT document_embeddings_pkey PRIMARY KEY (id);


--
-- Name: employees employees_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY employees
    ADD CONSTRAINT employees_email_key UNIQUE (email);


--
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);


--
-- Name: query_stats query_stats_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY query_stats
    ADD CONSTRAINT query_stats_pkey PRIMARY KEY (id);


--
-- Name: semantic_model semantic_model_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY semantic_model
    ADD CONSTRAINT semantic_model_pkey PRIMARY KEY (id);


--
-- Name: idx_agent_admin_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_admin_id ON agent USING btree (admin_id);


--
-- Name: idx_agent_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_category ON agent USING btree (category);


--
-- Name: idx_agent_datasource_agent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_datasource_agent_id ON agent_datasource USING btree (agent_id);


--
-- Name: idx_agent_datasource_datasource_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_datasource_datasource_id ON agent_datasource USING btree (datasource_id);


--
-- Name: idx_agent_datasource_is_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_datasource_is_active ON agent_datasource USING btree (is_active);


--
-- Name: idx_agent_knowledge_agent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_agent_id ON agent_knowledge USING btree (agent_id);


--
-- Name: idx_agent_knowledge_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_category ON agent_knowledge USING btree (category);


--
-- Name: idx_agent_knowledge_embedding_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_embedding_status ON agent_knowledge USING btree (embedding_status);


--
-- Name: idx_agent_knowledge_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_status ON agent_knowledge USING btree (status);


--
-- Name: idx_agent_knowledge_title; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_title ON agent_knowledge USING btree (title);


--
-- Name: idx_agent_knowledge_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_knowledge_type ON agent_knowledge USING btree (type);


--
-- Name: idx_agent_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_name ON agent USING btree (name);


--
-- Name: idx_agent_preset_question_agent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_preset_question_agent_id ON agent_preset_question USING btree (agent_id);


--
-- Name: idx_agent_preset_question_is_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_preset_question_is_active ON agent_preset_question USING btree (is_active);


--
-- Name: idx_agent_preset_question_sort_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_preset_question_sort_order ON agent_preset_question USING btree (sort_order);


--
-- Name: idx_agent_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_agent_status ON agent USING btree (status);


--
-- Name: idx_business_knowledge_agent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_knowledge_agent_id ON business_knowledge USING btree (agent_id);


--
-- Name: idx_business_knowledge_data_set_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_knowledge_data_set_id ON business_knowledge USING btree (data_set_id);


--
-- Name: idx_business_knowledge_is_recall; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_knowledge_is_recall ON business_knowledge USING btree (is_recall);


--
-- Name: idx_business_knowledge_term; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_knowledge_term ON business_knowledge USING btree (business_term);


--
-- Name: idx_datasource_creator_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_datasource_creator_id ON datasource USING btree (creator_id);


--
-- Name: idx_datasource_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_datasource_name ON datasource USING btree (name);


--
-- Name: idx_datasource_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_datasource_status ON datasource USING btree (status);


--
-- Name: idx_datasource_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_datasource_type ON datasource USING btree (type);


--
-- Name: idx_semantic_model_agent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_semantic_model_agent_id ON semantic_model USING btree (agent_id);


--
-- Name: idx_semantic_model_field_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_semantic_model_field_name ON semantic_model USING btree (field_name);


--
-- Name: idx_semantic_model_is_recall; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_semantic_model_is_recall ON semantic_model USING btree (is_recall);


--
-- Name: idx_semantic_model_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_semantic_model_status ON semantic_model USING btree (status);

--
-- Name: uk_agent_datasource; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_agent_datasource ON agent_datasource USING btree (agent_id, datasource_id);


--
-- PostgreSQL database dump complete
--