--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2015-12-28 02:49:12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 181 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 181
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 133507)
-- Name: android_reg_id; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE android_reg_id (
    id bigint NOT NULL,
    registration_id character varying(255),
    evento_id bigint
);


ALTER TABLE public.android_reg_id OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 133512)
-- Name: android_response; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE android_response (
    id bigint NOT NULL,
    canonicalids integer,
    failure integer NOT NULL,
    multicast_id bigint,
    success integer NOT NULL,
    evento_id bigint
);


ALTER TABLE public.android_response OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 133517)
-- Name: aplicacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE aplicacion (
    aplicacion_id bigint NOT NULL,
    api_key_dev character varying(255),
    api_key_prod character varying(255),
    certificado_dev character varying(255),
    certificado_prod character varying(255),
    error character varying(255),
    estado_android character varying(255),
    estado_ios character varying(255),
    key_file_dev character varying(255),
    key_file_prod character varying(255),
    nombre character varying(255),
    payload_size integer
);


ALTER TABLE public.aplicacion OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 133525)
-- Name: device_registration; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE device_registration (
    id bigint NOT NULL,
    accion character varying(255),
    cannonical_id character varying(255),
    error character varying(255),
    estado character varying(255),
    registration_id character varying(255),
    tipo character varying(255),
    aplicacion_id bigint
);


ALTER TABLE public.device_registration OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 133533)
-- Name: evento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE evento (
    id bigint NOT NULL,
    alert character varying(255),
    estado_android character varying(255),
    estado_ios character varying(255),
    prioridad character varying(255),
    production_mode boolean,
    send_to_sync boolean,
    android_response_id bigint,
    aplicacion_id bigint,
    ios_response_id bigint
);


ALTER TABLE public.evento OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 133618)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 133541)
-- Name: ios_reg_id; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ios_reg_id (
    id bigint NOT NULL,
    token character varying(255),
    evento_id bigint
);


ALTER TABLE public.ios_reg_id OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 133546)
-- Name: ios_response; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ios_response (
    id bigint NOT NULL,
    canonical_ids integer,
    error character varying(255),
    failure integer NOT NULL,
    success integer NOT NULL,
    evento_id bigint
);


ALTER TABLE public.ios_response OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 133551)
-- Name: message_result; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE message_result (
    id bigint NOT NULL,
    error character varying(255),
    message_id character varying(255),
    original_registration_id character varying(255),
    registration_id character varying(255),
    status integer,
    android_response_id bigint,
    ios_response_id bigint
);


ALTER TABLE public.message_result OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 133491)
-- Name: parametro; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE parametro (
    parametro_id bigint NOT NULL,
    nombre character varying(255),
    tipo_dato character varying(255),
    valor character varying(255)
);


ALTER TABLE public.parametro OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 133499)
-- Name: payload; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE payload (
    id bigint NOT NULL,
    clave character varying(255),
    valor character varying(255),
    evento_id bigint
);


ALTER TABLE public.payload OWNER TO postgres;

--
-- TOC entry 2009 (class 0 OID 133507)
-- Dependencies: 172
-- Data for Name: android_reg_id; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2010 (class 0 OID 133512)
-- Dependencies: 173
-- Data for Name: android_response; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2011 (class 0 OID 133517)
-- Dependencies: 174
-- Data for Name: aplicacion; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2012 (class 0 OID 133525)
-- Dependencies: 175
-- Data for Name: device_registration; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2013 (class 0 OID 133533)
-- Dependencies: 176
-- Data for Name: evento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 180
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 5, true);


--
-- TOC entry 2014 (class 0 OID 133541)
-- Dependencies: 177
-- Data for Name: ios_reg_id; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2015 (class 0 OID 133546)
-- Dependencies: 178
-- Data for Name: ios_response; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2016 (class 0 OID 133551)
-- Dependencies: 179
-- Data for Name: message_result; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2007 (class 0 OID 133491)
-- Dependencies: 170
-- Data for Name: parametro; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO parametro VALUES (1, 'PATH_CERTIFICADOS', 'String', 'C:\Users\Vanessa\Documents\work');
INSERT INTO parametro VALUES (2, 'URL_GCM', 'String', 'https://android.googleapis.com/gcm/send');
INSERT INTO parametro VALUES (3, 'IOS_THREADS', 'Integer', '3');
INSERT INTO parametro VALUES (4, 'IOS_TIMER', 'Integer', '60');
INSERT INTO parametro VALUES (5, 'ANDROID_TIMER', 'Integer', '50');


--
-- TOC entry 2008 (class 0 OID 133499)
-- Dependencies: 171
-- Data for Name: payload; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1872 (class 2606 OID 133511)
-- Name: android_reg_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY android_reg_id
    ADD CONSTRAINT android_reg_id_pkey PRIMARY KEY (id);


--
-- TOC entry 1874 (class 2606 OID 133516)
-- Name: android_response_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY android_response
    ADD CONSTRAINT android_response_pkey PRIMARY KEY (id);


--
-- TOC entry 1876 (class 2606 OID 133524)
-- Name: aplicacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT aplicacion_pkey PRIMARY KEY (aplicacion_id);


--
-- TOC entry 1880 (class 2606 OID 133532)
-- Name: device_registration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY device_registration
    ADD CONSTRAINT device_registration_pkey PRIMARY KEY (id);


--
-- TOC entry 1882 (class 2606 OID 133540)
-- Name: evento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT evento_pkey PRIMARY KEY (id);


--
-- TOC entry 1884 (class 2606 OID 133545)
-- Name: ios_reg_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ios_reg_id
    ADD CONSTRAINT ios_reg_id_pkey PRIMARY KEY (id);


--
-- TOC entry 1886 (class 2606 OID 133550)
-- Name: ios_response_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ios_response
    ADD CONSTRAINT ios_response_pkey PRIMARY KEY (id);


--
-- TOC entry 1888 (class 2606 OID 133558)
-- Name: message_result_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY message_result
    ADD CONSTRAINT message_result_pkey PRIMARY KEY (id);


--
-- TOC entry 1866 (class 2606 OID 133498)
-- Name: parametro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY parametro
    ADD CONSTRAINT parametro_pkey PRIMARY KEY (parametro_id);


--
-- TOC entry 1870 (class 2606 OID 133506)
-- Name: payload_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY payload
    ADD CONSTRAINT payload_pkey PRIMARY KEY (id);


--
-- TOC entry 1868 (class 2606 OID 133560)
-- Name: uk_3todcerrxtr2q14e6q9v1bnql; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY parametro
    ADD CONSTRAINT uk_3todcerrxtr2q14e6q9v1bnql UNIQUE (nombre);


--
-- TOC entry 1878 (class 2606 OID 133562)
-- Name: uk_fkcg5tjiioniol4pn4s7w6e5m; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT uk_fkcg5tjiioniol4pn4s7w6e5m UNIQUE (nombre);


--
-- TOC entry 1897 (class 2606 OID 133603)
-- Name: fk_18yo5m9r9a36cil3hqtnwejtj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ios_response
    ADD CONSTRAINT fk_18yo5m9r9a36cil3hqtnwejtj FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 1891 (class 2606 OID 133573)
-- Name: fk_4qad18wxdyn5q0q0barlfk0vq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY android_response
    ADD CONSTRAINT fk_4qad18wxdyn5q0q0barlfk0vq FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 1890 (class 2606 OID 133568)
-- Name: fk_4v7557d3owhox61cjf3kq6vj4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY android_reg_id
    ADD CONSTRAINT fk_4v7557d3owhox61cjf3kq6vj4 FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 1899 (class 2606 OID 133613)
-- Name: fk_70vow3f7jfx51p6ybeoi9ovqi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_result
    ADD CONSTRAINT fk_70vow3f7jfx51p6ybeoi9ovqi FOREIGN KEY (ios_response_id) REFERENCES ios_response(id);


--
-- TOC entry 1894 (class 2606 OID 133588)
-- Name: fk_cc3do3v4yo7tjaasphirldqcj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT fk_cc3do3v4yo7tjaasphirldqcj FOREIGN KEY (aplicacion_id) REFERENCES aplicacion(aplicacion_id);


--
-- TOC entry 1898 (class 2606 OID 133608)
-- Name: fk_f25hib6dwsfu0hcg983shuex9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY message_result
    ADD CONSTRAINT fk_f25hib6dwsfu0hcg983shuex9 FOREIGN KEY (android_response_id) REFERENCES android_response(id);


--
-- TOC entry 1889 (class 2606 OID 133563)
-- Name: fk_ghw3io502vdvv4a7yngp4ab8u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payload
    ADD CONSTRAINT fk_ghw3io502vdvv4a7yngp4ab8u FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 1893 (class 2606 OID 133583)
-- Name: fk_h0p4lsvj3ysl2wmrijiv05b58; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT fk_h0p4lsvj3ysl2wmrijiv05b58 FOREIGN KEY (android_response_id) REFERENCES android_response(id);


--
-- TOC entry 1895 (class 2606 OID 133593)
-- Name: fk_rukkpn3upaq3bgro8lhytymfh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT fk_rukkpn3upaq3bgro8lhytymfh FOREIGN KEY (ios_response_id) REFERENCES ios_response(id);


--
-- TOC entry 1892 (class 2606 OID 133578)
-- Name: fk_s5xba4tol50m2do26jljg5l8u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY device_registration
    ADD CONSTRAINT fk_s5xba4tol50m2do26jljg5l8u FOREIGN KEY (aplicacion_id) REFERENCES aplicacion(aplicacion_id);


--
-- TOC entry 1896 (class 2606 OID 133598)
-- Name: fk_tmu17kh9l59awnaltwhycp8mi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ios_reg_id
    ADD CONSTRAINT fk_tmu17kh9l59awnaltwhycp8mi FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-28 02:49:12

--
-- PostgreSQL database dump complete
--

