--
-- PostgreSQL database dump
--


-- Dumped from database version 18.6 (Debian 18.6-1.pgdg13+2)
-- Dumped by pg_dump version 18.6 (Debian 18.6-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.wishlist_items DROP CONSTRAINT IF EXISTS wishlist_items_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.wishlist_items DROP CONSTRAINT IF EXISTS wishlist_items_course_id_fkey;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_sender_id_fkey;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_conversation_id_fkey;
ALTER TABLE IF EXISTS ONLY public.disputes DROP CONSTRAINT IF EXISTS fkt7x5p1o0nm10e9ikdlucbx1u;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS fksh8qigpfv6e4c8opo01twjh5i;
ALTER TABLE IF EXISTS ONLY public.invalidated_token DROP CONSTRAINT IF EXISTS fkpv7k1rtxl6wjqu8o05oachfp1;
ALTER TABLE IF EXISTS ONLY public.courses DROP CONSTRAINT IF EXISTS fkp39d96v9hcwwij5bx2jm4g69u;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS fkoij4h3g5n6kn5g6743g7viepj;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS fknqd3rmo8lo3y5cncg277gvfiq;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS fkn3fs4986tsx5u32105n8q47wc;
ALTER TABLE IF EXISTS ONLY public.course_enrollments DROP CONSTRAINT IF EXISTS fkn0fy1fut1f7j4kxwxfl925qnd;
ALTER TABLE IF EXISTS ONLY public.disputes DROP CONSTRAINT IF EXISTS fkmbmkglcru85cdq73ih2ko4m1k;
ALTER TABLE IF EXISTS ONLY public.comments DROP CONSTRAINT IF EXISTS fklri30okf66phtcgbe5pok7cc0;
ALTER TABLE IF EXISTS ONLY public.payment_method DROP CONSTRAINT IF EXISTS fkkeh6a1awkqba7l425dq4bi0l3;
ALTER TABLE IF EXISTS ONLY public.comments DROP CONSTRAINT IF EXISTS fkk4ib6syde10dalk7r7xdl0m5p;
ALTER TABLE IF EXISTS ONLY public.email_verification_tokens DROP CONSTRAINT IF EXISTS fki1c4mmamlb8keqt74k4lrtwhc;
ALTER TABLE IF EXISTS ONLY public.bookings DROP CONSTRAINT IF EXISTS fki0lvs0c2wc4o2wyfi8bf0lwls;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS fkgruucsxmibdjhdldfywe39cxk;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS fkgcmnyjacr5dtpqf69kj8c4mtr;
ALTER TABLE IF EXISTS ONLY public.course_enrollments DROP CONSTRAINT IF EXISTS fkf78cq7ecdpk1clt1w5ofnb34t;
ALTER TABLE IF EXISTS ONLY public.withdrawal DROP CONSTRAINT IF EXISTS fke0lf36eoctbkgxwyqc5lo5wbj;
ALTER TABLE IF EXISTS ONLY public.articles DROP CONSTRAINT IF EXISTS fke02fs2ut6qqoabfhj325wcjul;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS fkd1isgfajhtdl8mgg29up6mofi;
ALTER TABLE IF EXISTS ONLY public.wallets DROP CONSTRAINT IF EXISTS fkc1foyisidw7wqqrkamafuwn4e;
ALTER TABLE IF EXISTS ONLY public.bookings DROP CONSTRAINT IF EXISTS fkar27os7sd4oj16rjdimbedgtf;
ALTER TABLE IF EXISTS ONLY public.action_logs DROP CONSTRAINT IF EXISTS fka0tll6fnhj3pla79m4t4r7ak5;
ALTER TABLE IF EXISTS ONLY public.action_logs DROP CONSTRAINT IF EXISTS fk8vramvvinxfmhna00oohk14cu;
ALTER TABLE IF EXISTS ONLY public.tutor_applications DROP CONSTRAINT IF EXISTS fk8uwa99u5cnpwbmpp1ccon2qgt;
ALTER TABLE IF EXISTS ONLY public.comments DROP CONSTRAINT IF EXISTS fk8omq0tc18jd43bu5tjh6jvraq;
ALTER TABLE IF EXISTS ONLY public.brokerage_packages DROP CONSTRAINT IF EXISTS fk89m851ye2278em1r1uvhrejty;
ALTER TABLE IF EXISTS ONLY public.disputes DROP CONSTRAINT IF EXISTS fk70i5mk4tv7uk491cadn733q0q;
ALTER TABLE IF EXISTS ONLY public.action_logs DROP CONSTRAINT IF EXISTS fk6locj4o5mo2ktklow0ywpfheg;
ALTER TABLE IF EXISTS ONLY public.bookings DROP CONSTRAINT IF EXISTS fk64axe7rxk1w0yybe1w9wwevv9;
ALTER TABLE IF EXISTS ONLY public.disputes DROP CONSTRAINT IF EXISTS fk4wgji1pxtiyvvsg3ft92tjl6v;
ALTER TABLE IF EXISTS ONLY public.conversations DROP CONSTRAINT IF EXISTS fk3tsdw1joa61a2a7cbrxq9w8g6;
ALTER TABLE IF EXISTS ONLY public.tutor_profiles DROP CONSTRAINT IF EXISTS fk3h4hc71bbx1xyce6kbjm1bagn;
ALTER TABLE IF EXISTS ONLY public.bookings DROP CONSTRAINT IF EXISTS fk3gcbjcudssgkxgwhkauoy5u4m;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS fk28gy0sw29lui429wk3be747t6;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS fk28an517hrxtt2bsg93uefugrm;
ALTER TABLE IF EXISTS ONLY public.conversations DROP CONSTRAINT IF EXISTS fk281qnh5jigvg90uiwrr865pbg;
ALTER TABLE IF EXISTS ONLY public.withdrawal DROP CONSTRAINT IF EXISTS fk21nf4qhjccx8nwq2sqjkvkpgx;
ALTER TABLE IF EXISTS ONLY public.lessons DROP CONSTRAINT IF EXISTS fk17ucc7gjfjddsyi0gvstkqeat;
ALTER TABLE IF EXISTS ONLY public.cart_items DROP CONSTRAINT IF EXISTS cart_items_user_id_fkey;
ALTER TABLE IF EXISTS ONLY public.cart_items DROP CONSTRAINT IF EXISTS cart_items_course_id_fkey;
DROP INDEX IF EXISTS public.idxl2kyy1r86epv8gdaxfifv1eq0;
DROP INDEX IF EXISTS public.idxk8oxftj2b4s3wlip1768dv4a;
DROP INDEX IF EXISTS public.idxieie5egigg2fh0pqvxihaayuy;
DROP INDEX IF EXISTS public.idx_wishlist_user;
DROP INDEX IF EXISTS public.idx_reviews_tutor_created;
DROP INDEX IF EXISTS public.idx_reviews_reviewer_created;
DROP INDEX IF EXISTS public.idx_messages_conversation_created;
DROP INDEX IF EXISTS public.idx_conversations_tutor_updated;
DROP INDEX IF EXISTS public.idx_conversations_student_updated;
DROP INDEX IF EXISTS public.idx_cart_user;
DROP INDEX IF EXISTS public.idx_action_log_user;
DROP INDEX IF EXISTS public.idx_action_log_target;
DROP INDEX IF EXISTS public.idx_action_log_created_at;
DROP INDEX IF EXISTS public.idx2jo41j3vk3i6v0lrh9mq61jjr;
DROP INDEX IF EXISTS public.idx2bdo6lu7jvkv8ke1mrnarqv9q;
ALTER TABLE IF EXISTS ONLY public.withdrawal DROP CONSTRAINT IF EXISTS withdrawal_pkey;
ALTER TABLE IF EXISTS ONLY public.wishlist_items DROP CONSTRAINT IF EXISTS wishlist_items_pkey;
ALTER TABLE IF EXISTS ONLY public.wallets DROP CONSTRAINT IF EXISTS wallets_pkey;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS ukt1i11uyqwk9gwl986p5kr010p;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS ukrq8aj69p6qdhxdt983bheuhdi;
ALTER TABLE IF EXISTS ONLY public.course_enrollments DROP CONSTRAINT IF EXISTS ukrjq0vqhmyin2pyjo9rnl8ykl1;
ALTER TABLE IF EXISTS ONLY public.tutor_applications DROP CONSTRAINT IF EXISTS ukhy3ouqbnq0w6qfqg5u0frp88l;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS ukb5pcsf1agn8v5asy2r569hylw;
ALTER TABLE IF EXISTS ONLY public.email_verification_tokens DROP CONSTRAINT IF EXISTS ukah12jdrm74nfnmu11x9xxlbex;
ALTER TABLE IF EXISTS ONLY public.wishlist_items DROP CONSTRAINT IF EXISTS uk_wishlist_user_course;
ALTER TABLE IF EXISTS ONLY public.cart_items DROP CONSTRAINT IF EXISTS uk_cart_user_course;
ALTER TABLE IF EXISTS ONLY public.payment_method DROP CONSTRAINT IF EXISTS uk9p2fk6b9gbuq4o4k3wrcvl7cp;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS uk6dotkott2kjsp8vw4d0m25fb7;
ALTER TABLE IF EXISTS ONLY public.wallets DROP CONSTRAINT IF EXISTS uk59hs4d6rv3ay5g42ny3ilxms0;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS uk3p9j9vyr1qofbcxju65es206r;
ALTER TABLE IF EXISTS ONLY public.tutor_profiles DROP CONSTRAINT IF EXISTS tutor_profiles_pkey;
ALTER TABLE IF EXISTS ONLY public.tutor_applications DROP CONSTRAINT IF EXISTS tutor_applications_pkey;
ALTER TABLE IF EXISTS ONLY public.reviews DROP CONSTRAINT IF EXISTS reviews_pkey;
ALTER TABLE IF EXISTS ONLY public.payment_transactions DROP CONSTRAINT IF EXISTS payment_transactions_pkey;
ALTER TABLE IF EXISTS ONLY public.payment_method DROP CONSTRAINT IF EXISTS payment_method_pkey;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_pkey;
ALTER TABLE IF EXISTS ONLY public.lessons DROP CONSTRAINT IF EXISTS lessons_pkey;
ALTER TABLE IF EXISTS ONLY public.invalidated_token DROP CONSTRAINT IF EXISTS invalidated_token_pkey;
ALTER TABLE IF EXISTS ONLY public.email_verification_tokens DROP CONSTRAINT IF EXISTS email_verification_tokens_pkey;
ALTER TABLE IF EXISTS ONLY public.disputes DROP CONSTRAINT IF EXISTS disputes_pkey;
ALTER TABLE IF EXISTS ONLY public.courses DROP CONSTRAINT IF EXISTS courses_pkey;
ALTER TABLE IF EXISTS ONLY public.course_enrollments DROP CONSTRAINT IF EXISTS course_enrollments_pkey;
ALTER TABLE IF EXISTS ONLY public.conversations DROP CONSTRAINT IF EXISTS conversations_pkey;
ALTER TABLE IF EXISTS ONLY public.comments DROP CONSTRAINT IF EXISTS comments_pkey;
ALTER TABLE IF EXISTS ONLY public.cart_items DROP CONSTRAINT IF EXISTS cart_items_pkey;
ALTER TABLE IF EXISTS ONLY public.brokerage_packages DROP CONSTRAINT IF EXISTS brokerage_packages_pkey;
ALTER TABLE IF EXISTS ONLY public.bookings DROP CONSTRAINT IF EXISTS bookings_pkey;
ALTER TABLE IF EXISTS ONLY public.articles DROP CONSTRAINT IF EXISTS articles_pkey;
ALTER TABLE IF EXISTS ONLY public.action_logs DROP CONSTRAINT IF EXISTS action_logs_pkey;
DROP TABLE IF EXISTS public.withdrawal;
DROP TABLE IF EXISTS public.wishlist_items;
DROP TABLE IF EXISTS public.wallets;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.tutor_profiles;
DROP TABLE IF EXISTS public.tutor_applications;
DROP TABLE IF EXISTS public.reviews;
DROP TABLE IF EXISTS public.payment_transactions;
DROP TABLE IF EXISTS public.payment_method;
DROP TABLE IF EXISTS public.messages;
DROP TABLE IF EXISTS public.lessons;
DROP TABLE IF EXISTS public.invalidated_token;
DROP TABLE IF EXISTS public.email_verification_tokens;
DROP TABLE IF EXISTS public.disputes;
DROP TABLE IF EXISTS public.courses;
DROP TABLE IF EXISTS public.course_enrollments;
DROP TABLE IF EXISTS public.conversations;
DROP TABLE IF EXISTS public.comments;
DROP TABLE IF EXISTS public.cart_items;
DROP TABLE IF EXISTS public.brokerage_packages;
DROP TABLE IF EXISTS public.bookings;
DROP TABLE IF EXISTS public.articles;
DROP TABLE IF EXISTS public.action_logs;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: action_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.action_logs (
    id character varying(255) NOT NULL,
    action_code character varying(100),
    created_at timestamp(6) with time zone NOT NULL,
    new_value jsonb,
    old_value jsonb,
    reason text,
    target_id character varying(255),
    target_type character varying(50),
    admin_id character varying(255),
    target_user_id character varying(255),
    action character varying(100) NOT NULL,
    ip_address character varying(255),
    user_agent text,
    user_id character varying(255),
    CONSTRAINT action_logs_action_check CHECK (((action)::text = ANY ((ARRAY['LOGIN_SUCCESS'::character varying, 'LOGIN_FAILED'::character varying, 'LOGOUT'::character varying, 'REGISTER'::character varying, 'UPDATE_PROFILE'::character varying, 'CHANGE_PASSWORD'::character varying, 'CREATE_USER'::character varying, 'UPDATE_USER'::character varying, 'DELETE_USER'::character varying, 'UPDATE_USER_STATUS'::character varying, 'CREATE_BOOKING'::character varying, 'CONFIRM_BOOKING'::character varying, 'COMPLETE_BOOKING'::character varying, 'CANCEL_BOOKING'::character varying, 'CREATE_COURSE'::character varying, 'UPDATE_COURSE'::character varying, 'DELETE_COURSE'::character varying, 'ENROLL_COURSE'::character varying, 'CANCEL_ENROLLMENT'::character varying, 'COMPLETE_ENROLLMENT'::character varying, 'DELETE_ENROLLMENT'::character varying, 'CREATE_LESSON'::character varying, 'UPDATE_LESSON'::character varying, 'DELETE_LESSON'::character varying, 'APPLY_TUTOR'::character varying, 'APPROVE_TUTOR_APPLICATION'::character varying, 'REJECT_TUTOR_APPLICATION'::character varying, 'DELETE_TUTOR_APPLICATION'::character varying, 'CREATE_DISPUTE'::character varying, 'CANCEL_DISPUTE'::character varying, 'APPROVE_DISPUTE'::character varying, 'REJECT_DISPUTE'::character varying, 'CREATE_COMMENT'::character varying, 'UPDATE_COMMENT'::character varying, 'DELETE_COMMENT'::character varying, 'CREATE_ARTICLE'::character varying, 'UPDATE_ARTICLE'::character varying, 'DELETE_ARTICLE'::character varying, 'CREATE_REVIEW'::character varying, 'UPDATE_REVIEW'::character varying, 'DELETE_REVIEW'::character varying, 'CREATE_CONVERSATION'::character varying, 'AGREE_CONVERSATION'::character varying, 'DECLINE_CONVERSATION'::character varying, 'SEND_MESSAGE'::character varying, 'UPDATE_MESSAGE'::character varying, 'DELETE_MESSAGE'::character varying, 'DEPOSIT_MONEY'::character varying, 'DEPOSIT_CONFIRMED'::character varying, 'WITHDRAW_MONEY'::character varying, 'CREATE_PAYMENT_TRANSACTION'::character varying, 'CREATE_PACKAGE'::character varying, 'UPDATE_PACKAGE'::character varying, 'SUSPEND_USER'::character varying, 'BAN_USER'::character varying, 'UNBAN_USER'::character varying])::text[])))
);


ALTER TABLE public.action_logs OWNER TO postgres;

--
-- Name: articles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.articles (
    id character varying(255) NOT NULL,
    author_avatar_url character varying(500),
    author_nickname character varying(150),
    content text NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    is_published boolean NOT NULL,
    min_role_level integer NOT NULL,
    title character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_id character varying(255) NOT NULL
);


ALTER TABLE public.articles OWNER TO postgres;

--
-- Name: bookings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookings (
    id character varying(255) NOT NULL,
    contact_unlocked_at timestamp(6) with time zone,
    created_at timestamp(6) with time zone NOT NULL,
    platform_fee bigint NOT NULL,
    status character varying(255) NOT NULL,
    student_confirmed_at timestamp(6) with time zone,
    student_rating integer,
    student_review text,
    subject character varying(100),
    total_price bigint NOT NULL,
    tutor_income bigint NOT NULL,
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    package_id character varying(255) NOT NULL,
    student_id character varying(255) NOT NULL,
    tutor_id character varying(255),
    conversation_id character varying(255) NOT NULL
);


ALTER TABLE public.bookings OWNER TO postgres;

--
-- Name: brokerage_packages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.brokerage_packages (
    id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    is_active boolean NOT NULL,
    name character varying(255) NOT NULL,
    platform_fee_percent integer NOT NULL,
    price bigint NOT NULL,
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    tutor_id character varying(255)
);


ALTER TABLE public.brokerage_packages OWNER TO postgres;

--
-- Name: cart_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart_items (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    course_id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL
);


ALTER TABLE public.cart_items OWNER TO postgres;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id character varying(255) NOT NULL,
    content text NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    is_hidden boolean NOT NULL,
    article_id character varying(255) NOT NULL,
    parent_id character varying(255),
    user_id character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: conversations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conversations (
    id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    status character varying(30) NOT NULL,
    student_agreed_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    student_id character varying(255) NOT NULL,
    tutor_id character varying(255) NOT NULL,
    CONSTRAINT conversations_status_check CHECK (((status)::text = ANY ((ARRAY['NEGOTIATING'::character varying, 'AGREED'::character varying, 'DECLINED'::character varying, 'EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.conversations OWNER TO postgres;

--
-- Name: course_enrollments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_enrollments (
    id character varying(255) NOT NULL,
    completed_at timestamp(6) with time zone,
    created_at timestamp(6) with time zone NOT NULL,
    price_paid bigint,
    progress_percent integer DEFAULT 0,
    status character varying(255),
    course_id character varying(255) NOT NULL,
    student_id character varying(255) NOT NULL,
    platform_fee bigint NOT NULL,
    total_price bigint NOT NULL,
    tutor_income bigint NOT NULL,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.course_enrollments OWNER TO postgres;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.courses (
    id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    is_active boolean NOT NULL,
    is_live boolean DEFAULT false,
    price bigint NOT NULL,
    title text NOT NULL,
    tutor_id character varying(255) NOT NULL,
    platform_fee_percent integer NOT NULL,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.courses OWNER TO postgres;

--
-- Name: disputes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.disputes (
    id character varying(255) NOT NULL,
    admin_note text,
    created_at timestamp(6) with time zone NOT NULL,
    reason text NOT NULL,
    refund_amount bigint NOT NULL,
    resolved_at timestamp(6) with time zone,
    status character varying(50) NOT NULL,
    booking_id character varying(255),
    student_id character varying(255),
    created_by_id character varying(255),
    evidence_url text,
    enrollment_id character varying(255),
    CONSTRAINT disputes_status_check CHECK (((status)::text = ANY ((ARRAY['UNDER_REVIEW'::character varying, 'CANCELLED'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.disputes OWNER TO postgres;

--
-- Name: email_verification_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_verification_tokens (
    id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    expires_at timestamp(6) with time zone NOT NULL,
    token_hash character varying(64) NOT NULL,
    used_at timestamp(6) with time zone,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.email_verification_tokens OWNER TO postgres;

--
-- Name: invalidated_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invalidated_token (
    id character varying(255) NOT NULL,
    expiry_time timestamp(6) without time zone,
    user_id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone
);


ALTER TABLE public.invalidated_token OWNER TO postgres;

--
-- Name: lessons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lessons (
    id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    media_url character varying(500),
    meeting_url character varying(500),
    starts_at timestamp(6) with time zone,
    title character varying(255) NOT NULL,
    course_id character varying(255) NOT NULL,
    ends_at timestamp(6) with time zone,
    lesson_order integer NOT NULL,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.lessons OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id character varying(255) NOT NULL,
    conversation_id character varying(255) NOT NULL,
    sender_id character varying(255) NOT NULL,
    content text NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    id character varying(255) NOT NULL,
    account_holder_name character varying(150),
    account_number character varying(50),
    bank_name character varying(100),
    is_default boolean NOT NULL,
    user_id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.payment_method OWNER TO postgres;

--
-- Name: payment_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_transactions (
    id character varying(255) NOT NULL,
    amount bigint NOT NULL,
    balance_after bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    note text,
    type character varying(255) NOT NULL,
    booking_id character varying(255),
    destination_wallet_id character varying(255),
    source_wallet_id character varying(255),
    updated_at timestamp(6) with time zone,
    provider character varying(255),
    provider_reference character varying(150),
    reference_code character varying(100) NOT NULL,
    status character varying(255) NOT NULL,
    enrollment_id character varying(255),
    withdrawal_id character varying(255),
    CONSTRAINT payment_transactions_provider_check CHECK (((provider)::text = ANY ((ARRAY['MOCK'::character varying, 'VNPAY'::character varying, 'MOMO'::character varying, 'INTERNAL'::character varying])::text[]))),
    CONSTRAINT payment_transactions_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'SUCCESS'::character varying, 'FAILED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT payment_transactions_type_check CHECK (((type)::text = ANY ((ARRAY['DEPOSIT'::character varying, 'PAYMENT'::character varying, 'ESCROW_HOLD'::character varying, 'ESCROW_RELEASE'::character varying, 'WITHDRAW'::character varying, 'REFUND'::character varying])::text[])))
);


ALTER TABLE public.payment_transactions OWNER TO postgres;

--
-- Name: reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reviews (
    id character varying(255) NOT NULL,
    comment text,
    created_at timestamp(6) with time zone NOT NULL,
    rating integer NOT NULL,
    updated_at timestamp(6) with time zone,
    booking_id character varying(255),
    enrollment_id character varying(255),
    reviewer_id character varying(255) NOT NULL,
    tutor_id character varying(255) NOT NULL
);


ALTER TABLE public.reviews OWNER TO postgres;

--
-- Name: tutor_applications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tutor_applications (
    id character varying(255) NOT NULL,
    admin_note text,
    bio text NOT NULL,
    created_at timestamp(6) with time zone,
    cv_url text NOT NULL,
    status character varying(255) NOT NULL,
    subjects_can_teach text NOT NULL,
    test_submission text,
    update_at timestamp(6) with time zone,
    user_id character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone,
    CONSTRAINT tutor_applications_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.tutor_applications OWNER TO postgres;

--
-- Name: tutor_profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tutor_profiles (
    user_id character varying(255) NOT NULL,
    bio text,
    created_at timestamp(6) with time zone NOT NULL,
    rating numeric(3,2),
    subjects_can_teach text,
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    available_schedule text
);


ALTER TABLE public.tutor_profiles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id character varying(255) NOT NULL,
    avatar_url text,
    created_at timestamp(6) with time zone,
    display_name character varying(150) NOT NULL,
    email character varying(255) NOT NULL,
    email_verified_at timestamp(6) with time zone,
    password_hash character varying(255) NOT NULL,
    phone character varying(20),
    role character varying(255) NOT NULL,
    status character varying(255),
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    zalo_link text,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['GUEST'::character varying, 'STUDENT'::character varying, 'TUTOR'::character varying, 'ADMIN'::character varying])::text[]))),
    CONSTRAINT users_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'SUSPENDED'::character varying, 'BANNED'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: wallets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wallets (
    id character varying(255) NOT NULL,
    available_balance bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    holding_balance bigint NOT NULL,
    type character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id character varying(255),
    CONSTRAINT wallets_type_check CHECK (((type)::text = ANY ((ARRAY['NORMAL'::character varying, 'COMMERCIAL'::character varying, 'PLATFORM'::character varying])::text[])))
);


ALTER TABLE public.wallets OWNER TO postgres;

--
-- Name: wishlist_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wishlist_items (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    course_id character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL
);


ALTER TABLE public.wishlist_items OWNER TO postgres;

--
-- Name: withdrawal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.withdrawal (
    id character varying(255) NOT NULL,
    account_holder character varying(255) NOT NULL,
    account_number character varying(255) NOT NULL,
    amount bigint NOT NULL,
    bank_name character varying(255) NOT NULL,
    created_at timestamp(6) with time zone,
    failure_reason character varying(255),
    processed_at timestamp(6) with time zone,
    provider_reference character varying(255),
    status character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    wallet_id character varying(255) NOT NULL,
    CONSTRAINT withdrawal_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PROCESSING'::character varying, 'SUCCESS'::character varying, 'FAILED'::character varying])::text[])))
);


ALTER TABLE public.withdrawal OWNER TO postgres;

--
-- Data for Name: action_logs; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('3da4af8f-20cc-4aca-9431-0f6c4a0bb9cc', NULL, '2026-11-04 09:04:09.494619+00', NULL, NULL, NULL, '1d1b2346-092c-4529-a34a-0c84a77a530d', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', '1d1b2346-092c-4529-a34a-0c84a77a530d');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('833725c0-0a0a-4014-833e-7526f89bf057', NULL, '2026-11-04 09:05:35.458524+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('b253dcc9-a907-4f0f-b9f0-08595cb090e9', NULL, '2026-11-04 09:07:22.947442+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('93571385-ec98-474c-aed7-8bc05658dc00', NULL, '2026-11-04 09:08:03.606757+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('4e2ab71f-16f2-4c7d-bb20-63032a0b8ea1', NULL, '2026-11-04 09:12:42.306072+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('e0341662-1b15-48a5-9a73-46606ded4596', NULL, '2026-11-13 11:16:19.370977+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('fee3b8a6-06ed-4d52-a506-f9dd6a556a42', NULL, '2026-11-04 09:13:39.682273+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('8116599f-e3f5-4be3-9621-2e9cd480667e', NULL, '2026-11-13 11:16:55.653898+00', NULL, NULL, NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', 'Conversation', NULL, NULL, 'CREATE_CONVERSATION', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('2ae2edad-e009-462d-995f-3662c6aaf433', NULL, '2026-11-13 11:18:42.55594+00', NULL, NULL, NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', 'Conversation', NULL, NULL, 'AGREE_CONVERSATION', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('8a6430e0-8b3b-4fe5-936e-1d438a7c16d0', NULL, '2026-11-13 11:18:44.876015+00', NULL, NULL, NULL, '0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('88a02f2b-46ed-49c6-920e-f488db83dbc1', NULL, '2026-11-13 11:26:50.867276+00', NULL, NULL, NULL, '455f7c52-4e88-4e9e-8fab-2520cf27a005', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '0:0:0:0:0:0:0:1', NULL, '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('6a094ccb-f9a7-407c-b5d7-86a3135f319f', NULL, '2026-11-13 11:30:03.327278+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('7da82ba9-1014-482e-86c0-175267f29b00', NULL, '2026-11-13 11:30:39.534574+00', NULL, NULL, NULL, 'ce43efe4-ee0a-4f63-a62d-de16b404bf0d', 'CourseEnrollment', NULL, NULL, 'ENROLL_COURSE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('5336205f-f143-4789-9350-7ef2f78a6395', NULL, '2026-11-13 11:31:25.721368+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('1b5fdc1a-c99c-4714-87ac-a025562212af', NULL, '2026-11-13 11:32:28.15501+00', NULL, NULL, NULL, 'dc7b96ef-f014-4dc1-9147-a702fb6e9242', 'Message', NULL, NULL, 'SEND_MESSAGE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('aa73c14b-aab9-415d-be1b-7274f0eaccfd', NULL, '2026-11-13 11:33:37.307266+00', NULL, NULL, NULL, '14d2066a-03ed-40af-9692-a3fa83c26c14', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('1edc63c9-bb9b-47bb-b2e1-b8f9c782b71b', NULL, '2026-11-13 11:33:48.740117+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('1b391ef1-beee-46c3-ab97-fe788bbdc564', NULL, '2026-11-13 11:34:07.598782+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('dcbfe34e-94a3-455e-a19a-4a555948bbdc', NULL, '2026-11-13 11:34:17.612946+00', NULL, NULL, NULL, '1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('45a10852-5770-47e9-adc4-c84e23c6873c', NULL, '2026-11-13 11:54:56.068865+00', NULL, NULL, NULL, '090411b7-8f84-4dc5-aced-2189dcc266f0', 'Booking', NULL, NULL, 'COMPLETE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('5ab3ad01-879d-4840-8b8d-89acad931679', NULL, '2026-11-07 10:04:37.33297+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('6173d14f-f234-47e2-8193-f30845e8eae8', NULL, '2026-11-08 09:00:44.691005+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('14646b61-c107-47d9-a680-4903f3b3d29e', NULL, '2026-11-13 11:16:27.082474+00', NULL, NULL, NULL, 'cdcd1bb0-2fc7-4512-be89-4e360dbd0978', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('76b99197-bf60-4b8a-8ea7-4d77e2df7606', NULL, '2026-11-08 09:08:56.342948+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('8585f690-16b6-4508-b68c-6cecf048e16d', NULL, '2026-11-08 09:13:40.003518+00', NULL, NULL, NULL, '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033', 'Authentication', NULL, NULL, 'LOGIN_FAILED', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('8f51ba1e-e586-443e-962a-842934cbabd8', NULL, '2026-11-08 09:14:39.054838+00', NULL, NULL, NULL, '1d1b2346-092c-4529-a34a-0c84a77a530d', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', '1d1b2346-092c-4529-a34a-0c84a77a530d');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('eb0ef7d3-ccbe-4aae-aad9-8916f986b022', NULL, '2026-11-13 11:16:27.348711+00', NULL, NULL, NULL, 'cdcd1bb0-2fc7-4512-be89-4e360dbd0978', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('a324f60f-2594-439c-bc85-62c8a1a83162', NULL, '2026-11-13 11:18:42.55594+00', NULL, NULL, NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', 'Conversation', NULL, NULL, 'AGREE_CONVERSATION', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('2c6a54f8-7425-4f8b-8356-5d6308e867b5', NULL, '2026-11-13 11:22:03.836845+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('cff2db22-b0c6-4a2f-b788-5816f529f69d', NULL, '2026-11-13 11:31:13.197848+00', NULL, NULL, NULL, 'f11cb76a-5049-41f2-92a7-aedbb45627d1', 'Dispute', NULL, NULL, 'CREATE_DISPUTE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('9481deb0-fe9f-4fe8-a2dc-c14867ea90ad', NULL, '2026-11-13 11:32:20.283315+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('e6ee020f-083f-44ff-ab4d-5f773758ad5f', NULL, '2026-11-13 11:32:37.990212+00', NULL, NULL, NULL, 'a7543f3c-818b-479d-8fc3-ecce4b77f048', 'Message', NULL, NULL, 'SEND_MESSAGE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('776483df-8296-485e-8273-39343a69d113', NULL, '2026-11-13 11:58:39.315276+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('7440d846-2a32-44d7-bf25-5670bc9bd6cb', NULL, '2026-11-13 11:58:50.800715+00', NULL, NULL, NULL, '30cbaac5-a36c-4782-a193-e50b2374d42e', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('4aabd38e-b19e-409a-895f-dfc2ad49c43b', NULL, '2026-11-13 11:59:06.779687+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('f12efb10-4354-4510-81ea-d92ef78bffa7', NULL, '2026-11-08 09:14:53.269257+00', NULL, NULL, NULL, '1d1b2346-092c-4529-a34a-0c84a77a530d', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '0:0:0:0:0:0:0:1', 'Python-urllib/3.13', '1d1b2346-092c-4529-a34a-0c84a77a530d');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('c06732b7-ba91-4179-ace3-32dc90b5ea72', NULL, '2026-11-13 11:22:11.46022+00', NULL, NULL, NULL, 'ca2cf1fc-9a26-4f3a-a354-624f2d860685', 'CourseEnrollment', NULL, NULL, 'ENROLL_COURSE', '0:0:0:0:0:0:0:1', NULL, '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('0ba6d3a0-87d1-4029-8081-0a3ad8e0d54e', NULL, '2026-11-13 11:30:30.647366+00', NULL, NULL, NULL, '7c85191f-bfb2-4f34-96a9-a6e8f2abbb8a', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('62a812e0-4db4-4c87-a53d-17d94984e419', NULL, '2026-11-13 11:30:30.712443+00', NULL, NULL, NULL, '7c85191f-bfb2-4f34-96a9-a6e8f2abbb8a', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('e7b9b660-9dbb-4662-8828-48e7bc3b137e', NULL, '2026-11-13 11:33:37.482923+00', NULL, NULL, NULL, '14d2066a-03ed-40af-9692-a3fa83c26c14', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('0b132bdb-ea4a-47f2-9ee7-494ac0cb96b5', NULL, '2026-11-13 11:34:38.57393+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('92b85c97-b400-4460-a04f-289988df4fd9', NULL, '2026-11-14 02:24:12.531899+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('297de9c1-2610-4066-9bfc-a9e6318119f6', NULL, '2026-11-08 09:15:07.300205+00', NULL, NULL, NULL, '22222222-aaaa-bbbb-cccc-000000000003', 'Lesson', NULL, NULL, 'UPDATE_LESSON', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('59ed71c4-73c2-4447-86e0-bae40aca2fdf', NULL, '2026-11-13 11:26:50.901548+00', NULL, NULL, NULL, '455f7c52-4e88-4e9e-8fab-2520cf27a005', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '0:0:0:0:0:0:0:1', NULL, '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('f14f5a12-4da4-4497-9e7c-939dd680dd39', NULL, '2026-11-13 11:33:11.8615+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('9cccc646-4cd2-470e-a84a-44534c6d5d91', NULL, '2026-11-09 02:56:22.964743+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('7d7dcab3-8a12-4008-bed5-2576fdea9e0b', NULL, '2026-11-14 02:30:42.423554+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('fc3a5d50-7930-433d-a4a2-11322be6d506', NULL, '2026-11-09 03:10:14.817798+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('879a9eb6-1a75-411f-b7f2-8ef4c284baf5', NULL, '2026-11-14 02:35:51.799897+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('82a4f810-f046-4b8a-a056-7cf98eae2072', NULL, '2026-11-09 03:15:03.494939+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('c2ad6ba2-158b-419e-8fde-a7e1183930ad', NULL, '2026-11-09 03:29:06.137792+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('6421632d-abb1-4f13-97f2-2f07003b0b50', NULL, '2026-11-09 03:30:35.249112+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('8b061da3-2791-4b7c-8d86-6def40abc374', NULL, '2026-11-09 03:35:07.433389+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('1f32a2f8-afa8-47e7-8dc6-873b2304228d', NULL, '2026-11-09 03:39:59.868109+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('e14759d4-f572-40d9-9995-f93232ea02b9', NULL, '2026-11-09 03:41:41.453335+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('a3d11e42-d323-4d28-b381-9dbd1d3a941e', NULL, '2026-11-13 11:31:40.147656+00', NULL, NULL, NULL, 'f11cb76a-5049-41f2-92a7-aedbb45627d1', 'Dispute', NULL, NULL, 'APPROVE_DISPUTE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('7175a3e5-95e1-493a-bdea-a29ac8742197', NULL, '2026-11-09 04:41:05.951213+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('04e3f735-cca6-4529-8184-80c21632f918', NULL, '2026-11-14 03:18:03.371655+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('a4e71dbb-62a5-4db1-8bcb-a09d9485cdbd', NULL, '2026-11-14 03:18:38.435539+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('988524fc-e0d7-4b19-8c43-1fe893836f4f', NULL, '2026-11-09 04:55:03.015691+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('d23fce71-161f-4796-a693-f5dc95bbfeeb', NULL, '2026-11-09 04:55:27.473277+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('88b4e417-82d0-456a-9014-76759554e2c3', NULL, '2026-11-09 04:57:07.857621+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('32701d65-cf6b-4135-9d56-b55e77877978', NULL, '2026-11-09 07:08:01.889578+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('fa97cf16-5afe-4058-965a-52dc3ea45f2b', NULL, '2026-11-09 07:09:07.759759+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('3618ce01-9e8c-410a-a530-8ab551b3fb80', NULL, '2026-11-09 07:09:13.173886+00', NULL, NULL, NULL, '40ca371f-db26-4d39-8f79-dc83644749be', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('fb38100a-5c76-4a21-8ba3-dc250840417e', NULL, '2026-11-09 07:38:07.433701+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('b69ff155-ba65-4b41-945b-fcf62420e5c5', NULL, '2026-11-09 07:39:35.092582+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('118ed31c-fa79-4acb-a4d4-c51cdde5de41', NULL, '2026-11-09 07:40:28.678106+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('58656cab-b4ca-4209-8327-0dcffb1ac857', NULL, '2026-11-13 11:31:47.530043+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('28784cc5-5579-4dad-8bc8-14eb650c3e69', NULL, '2026-11-14 03:19:36.789648+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('babcfa77-e025-4beb-b1f3-08ecc0214af9', NULL, '2026-11-09 07:42:04.8711+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('23326955-6ec2-4746-88c7-54e280e5785f', NULL, '2026-11-13 11:40:29.498373+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('4a142d0c-bb05-41b9-ae1a-f43d64995225', NULL, '2026-11-13 11:40:48.328511+00', NULL, NULL, NULL, '090411b7-8f84-4dc5-aced-2189dcc266f0', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('fb64bb9e-cfec-49c3-98ae-cf5075bba094', NULL, '2026-11-13 11:54:47.54071+00', NULL, NULL, NULL, '1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'Booking', NULL, NULL, 'COMPLETE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('edf50f7c-9b7d-4721-85a7-6d176dd4c37b', NULL, '2026-11-09 08:30:29.148521+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('a788290b-48cc-4fd7-bbd6-c760e14a8707', NULL, '2026-11-09 08:31:13.220219+00', NULL, NULL, NULL, 'a0000000-0000-0000-0000-000000000001', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('eded4034-4941-4685-abd5-c23f607c03d2', NULL, '2026-11-13 11:55:56.944372+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('28496f03-6512-485e-b3a2-496cffd800f9', NULL, '2026-11-13 11:57:54.275446+00', NULL, NULL, NULL, '7c9cdc9d-ddfd-4265-987f-a10848375b90', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('74db880a-271a-4196-bd74-ade1e9d49939', NULL, '2026-11-09 16:13:46.476671+00', NULL, NULL, NULL, '29d67e32-a9c4-4bd9-8188-2029a168587e', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '0:0:0:0:0:0:0:1', NULL, 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('03912480-d45c-47fc-9a23-b7231b0b449b', NULL, '2026-11-09 16:13:46.539859+00', NULL, NULL, NULL, '29d67e32-a9c4-4bd9-8188-2029a168587e', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '0:0:0:0:0:0:0:1', NULL, 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('405fd8f2-ad55-4481-a287-0434c9700b6b', NULL, '2026-11-09 16:13:46.616221+00', NULL, NULL, NULL, '47c9dc38-807f-493a-a5dd-4364938f2b49', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_MONEY', '0:0:0:0:0:0:0:1', NULL, '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('2a34dbee-c0ed-4ac2-a535-af1e09e9b660', NULL, '2026-11-09 16:13:46.675366+00', NULL, NULL, NULL, '47c9dc38-807f-493a-a5dd-4364938f2b49', 'PaymentTransaction', NULL, NULL, 'DEPOSIT_CONFIRMED', '0:0:0:0:0:0:0:1', NULL, '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('ce4e620e-cac0-4be2-8c70-0ad1584a2e79', NULL, '2026-11-13 11:58:09.014782+00', NULL, NULL, NULL, '7c9cdc9d-ddfd-4265-987f-a10848375b90', 'Booking', NULL, NULL, 'COMPLETE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('bb1feba4-1c92-4f06-91da-acb8e208ebe3', NULL, '2026-11-14 03:28:12.665344+00', NULL, NULL, NULL, '5761585a-b4b0-4f8b-af8e-2a929601fc8c', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('b13ee056-74b2-4815-b388-711304d3c284', NULL, '2026-11-14 03:28:32.714018+00', NULL, NULL, NULL, 'bb09b285-34e6-4147-88c9-0dfffa387018', 'Booking', NULL, NULL, 'CREATE_BOOKING', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('d4f3161f-a250-4425-a947-0abe947b62bb', NULL, '2026-11-14 03:29:28.54554+00', NULL, NULL, NULL, 'b774670b-6875-4835-b2bb-c8b3430d7b98', 'Message', NULL, NULL, 'SEND_MESSAGE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('f711907a-c15c-4e38-871c-3787103abfda', NULL, '2026-11-14 03:29:42.352843+00', NULL, NULL, NULL, 'cc16bee9-cdb7-47a3-9cc4-67d3b4e7846d', 'Message', NULL, NULL, 'SEND_MESSAGE', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('656d0743-d2b0-440d-aa8a-eba5485bf511', NULL, '2026-11-13 11:08:49.58527+00', NULL, NULL, NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Authentication', NULL, NULL, 'LOGIN_SUCCESS', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.action_logs (id, action_code, created_at, new_value, old_value, reason, target_id, target_type, admin_id, target_user_id, action, ip_address, user_agent, user_id) VALUES ('ef2ca348-cb77-457d-95d1-2876d4e03d43', NULL, '2026-11-13 11:15:14.909334+00', NULL, NULL, NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'User', NULL, NULL, 'CREATE_USER', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36', '7b029d9a-4246-4273-be13-7a53ae121011');


--
-- Data for Name: articles; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('30cbaac5-a36c-4782-a193-e50b2374d42e', NULL, '2026-11-13 11:58:50.800072+00', 45000, 'PENDING_CONFIRMATION', NULL, NULL, NULL, NULL, 450000, 405000, '2026-11-13 11:58:53.925316+00', '33333333-aaaa-bbbb-cccc-000000000001', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('5761585a-b4b0-4f8b-af8e-2a929601fc8c', NULL, '2026-11-14 03:28:12.647106+00', 90000, 'PENDING_PAYMENT', NULL, NULL, NULL, NULL, 450000, 360000, '2026-11-14 03:28:12.647106+00', 'd61c22ae-45b4-4aec-9fff-47d3758f3664', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('bb09b285-34e6-4147-88c9-0dfffa387018', NULL, '2026-11-14 03:28:32.714018+00', 90000, 'PENDING_CONFIRMATION', NULL, NULL, NULL, NULL, 450000, 360000, '2026-11-14 03:28:37.974144+00', 'd61c22ae-45b4-4aec-9fff-47d3758f3664', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', NULL, '2026-11-13 11:18:44.856547+00', 150000, 'CANCELLED', NULL, NULL, NULL, NULL, 150000, 0, '2026-11-13 11:31:40.147656+00', '33333333-aaaa-bbbb-cccc-000000000010', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', NULL, '2026-11-13 11:34:17.61241+00', 300000, 'COMPLETED', NULL, NULL, NULL, NULL, 300000, 0, '2026-11-13 11:54:47.563152+00', '33333333-aaaa-bbbb-cccc-000000000020', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('090411b7-8f84-4dc5-aced-2189dcc266f0', NULL, '2026-11-13 11:40:48.328511+00', 300000, 'COMPLETED', NULL, NULL, NULL, NULL, 300000, 0, '2026-11-13 11:54:56.089553+00', '33333333-aaaa-bbbb-cccc-000000000020', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');
INSERT INTO public.bookings (id, contact_unlocked_at, created_at, platform_fee, status, student_confirmed_at, student_rating, student_review, subject, total_price, tutor_income, updated_at, package_id, student_id, tutor_id, conversation_id) VALUES ('7c9cdc9d-ddfd-4265-987f-a10848375b90', NULL, '2026-11-13 11:57:54.275446+00', 260000, 'COMPLETED', NULL, NULL, NULL, NULL, 2600000, 2340000, '2026-11-13 11:58:09.021309+00', '33333333-aaaa-bbbb-cccc-000000000003', '7b029d9a-4246-4273-be13-7a53ae121011', NULL, '52a2e0ed-e2ec-48be-bcb8-f8b11272a350');


--
-- Data for Name: brokerage_packages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('33333333-aaaa-bbbb-cccc-000000000001', '2026-10-28 17:32:33.458942+00', true, '[Gói Trực Tuyến 1-1] 3 Buổi Kèm Riêng Qua Google Meet & Ký Quỹ Escrow', 10, 450000, '2026-10-28 17:32:33.458942+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('33333333-aaaa-bbbb-cccc-000000000002', '2026-10-28 17:32:33.458942+00', true, '[Gói Trực Tuyến 1-1] 10 Buổi Kèm Chuyên Sâu Qua Google Meet & Ký Quỹ Escrow', 10, 1400000, '2026-10-28 17:32:33.458942+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('33333333-aaaa-bbbb-cccc-000000000003', '2026-10-28 17:32:33.458942+00', true, '[Gói Trực Tuyến 1-1] 20 Buổi Luyện Thi Cấp Tốc Qua Google Meet & Ký Quỹ Escrow', 10, 2600000, '2026-10-28 17:32:33.458942+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('33333333-aaaa-bbbb-cccc-000000000010', '2026-10-31 07:52:50.74017+00', true, '[Gói Môi Giới Ngoài] Phí Kết Nối Môi Giới Tiêu Chuẩn (Hai bên tự dạy ngoài)', 100, 150000, '2026-10-31 07:52:50.74017+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('33333333-aaaa-bbbb-cccc-000000000020', '2026-10-31 07:52:50.74017+00', true, '[Gói Môi Giới Ngoài] Phí Kết Nối Môi Giới VIP + Bảo Hành Đổi Gia Sư (Tự dạy ngoài)', 100, 300000, '2026-10-31 07:52:50.74017+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('66c7a54c-abc2-4a35-a1df-ec2d64c27e18', '2026-11-14 03:18:24.8598+00', true, 'Luyện thi Lý cấp tốc', 10, 450000, '2026-11-14 03:18:24.8598+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('e6d0693b-c606-46ef-abdd-85dd833eee72', '2026-11-14 03:20:34.473177+00', true, 'Luyện Lý cấp tốc', 10, 450000, '2026-11-14 03:20:34.473177+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.brokerage_packages (id, created_at, is_active, name, platform_fee_percent, price, updated_at, tutor_id) VALUES ('d61c22ae-45b4-4aec-9fff-47d3758f3664', '2026-11-14 03:27:36.659231+00', true, 'Ôn cấp tốc Lý', 20, 450000, '2026-11-14 03:27:36.659231+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');


--
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: conversations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.conversations (id, created_at, status, student_agreed_at, updated_at, student_id, tutor_id) VALUES ('22b04ba7-c5ea-45a8-b1b5-f303f00fba5d', '2026-10-28 17:36:42.089997+00', 'NEGOTIATING', NULL, '2026-10-28 17:36:42.12524+00', '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.conversations (id, created_at, status, student_agreed_at, updated_at, student_id, tutor_id) VALUES ('52a2e0ed-e2ec-48be-bcb8-f8b11272a350', '2026-11-13 11:16:55.653392+00', 'AGREED', '2026-11-13 11:18:42.55594+00', '2026-11-13 11:18:42.613927+00', '7b029d9a-4246-4273-be13-7a53ae121011', '63d91ca1-d606-4d51-849c-c90749a7bd5c');


--
-- Data for Name: course_enrollments; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.course_enrollments (id, completed_at, created_at, price_paid, progress_percent, status, course_id, student_id, platform_fee, total_price, tutor_income, updated_at) VALUES ('1a54bf2a-db31-42d2-9aa2-55c3c0727499', NULL, '2026-10-28 17:04:53.707218+00', 499000, 0, 'ACTIVE', '11111111-aaaa-bbbb-cccc-000000000001', '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033', 49900, 499000, 449100, '2026-10-28 17:04:53.707218+00');
INSERT INTO public.course_enrollments (id, completed_at, created_at, price_paid, progress_percent, status, course_id, student_id, platform_fee, total_price, tutor_income, updated_at) VALUES ('ca2cf1fc-9a26-4f3a-a354-624f2d860685', NULL, '2026-11-13 11:22:11.46022+00', 499000, 0, 'ACTIVE', '11111111-aaaa-bbbb-cccc-000000000001', '7b029d9a-4246-4273-be13-7a53ae121011', 49900, 499000, 449100, '2026-11-13 11:26:50.956905+00');
INSERT INTO public.course_enrollments (id, completed_at, created_at, price_paid, progress_percent, status, course_id, student_id, platform_fee, total_price, tutor_income, updated_at) VALUES ('ce43efe4-ee0a-4f63-a62d-de16b404bf0d', NULL, '2026-11-13 11:30:39.534068+00', 699000, 0, 'ACTIVE', '11111111-aaaa-bbbb-cccc-000000000002', '7b029d9a-4246-4273-be13-7a53ae121011', 69900, 699000, 629100, '2026-11-13 11:30:41.19299+00');


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.courses (id, created_at, is_active, is_live, price, title, tutor_id, platform_fee_percent, updated_at) VALUES ('11111111-aaaa-bbbb-cccc-000000000001', '2026-10-28 16:41:58.241903+00', true, false, 499000, 'Chinh Phục Toán 12 & Ôn Thi THPT Quốc Gia Điểm 9+', '63d91ca1-d606-4d51-849c-c90749a7bd5c', 10, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.courses (id, created_at, is_active, is_live, price, title, tutor_id, platform_fee_percent, updated_at) VALUES ('11111111-aaaa-bbbb-cccc-000000000002', '2026-10-28 16:41:58.241903+00', true, false, 699000, 'Lập Trình Web Fullstack Java Spring Boot & React từ Zero đến Hero', '63d91ca1-d606-4d51-849c-c90749a7bd5c', 10, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.courses (id, created_at, is_active, is_live, price, title, tutor_id, platform_fee_percent, updated_at) VALUES ('11111111-aaaa-bbbb-cccc-000000000003', '2026-10-28 16:41:58.241903+00', true, false, 550000, 'Bứt Phá Tiếng Anh Giao Tiếp & Luyện Thi IELTS 6.5+ Cấp Tốc', '63d91ca1-d606-4d51-849c-c90749a7bd5c', 10, '2026-10-28 16:41:58.241903+00');


--
-- Data for Name: disputes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.disputes (id, admin_note, created_at, reason, refund_amount, resolved_at, status, booking_id, student_id, created_by_id, evidence_url, enrollment_id) VALUES ('disp-00000002-test-course-content', NULL, '2026-11-04 09:07:57.198986+00', 'Khoa hoc chuyen de Hinh hoc khong gian bi loi video mat tieng va thieu tai lieu dinh kem nhu cam ket.', 450000, NULL, 'UNDER_REVIEW', NULL, NULL, '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033', 'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?auto=format&fit=crop&w=800&q=80', NULL);
INSERT INTO public.disputes (id, admin_note, created_at, reason, refund_amount, resolved_at, status, booking_id, student_id, created_by_id, evidence_url, enrollment_id) VALUES ('f11cb76a-5049-41f2-92a7-aedbb45627d1', 'Your dispute has been approved by the creator', '2026-11-13 11:31:13.197319+00', 'dfdbzsdfbzsfb', 0, '2026-11-13 11:31:40.147046+00', 'APPROVED', '0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', NULL, '7b029d9a-4246-4273-be13-7a53ae121011', 'https://github.com/', NULL);


--
-- Data for Name: email_verification_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('f32607f9-1444-43b0-b2e5-7fcefa3fdcec', '2026-10-20 03:45:15.702976+00', '2026-10-20 04:00:15.702976+00', 'f02a79d3a86f1c08e9cfa6e8baf78cdd19120b8ebc0608dba828f0bc52a4f9e2', NULL, '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('c8f0b93e-af00-445f-b604-5151507c211f', '2026-10-20 09:56:34.53194+00', '2026-10-20 10:11:34.53194+00', 'ddc8b7d8ba512d39234408fdaa05bb811a5301738c8e11f4e786346c49cfa4e7', NULL, '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033');
INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('434ad2ee-6dd4-4c49-9d1e-753b0fab7c00', '2026-10-20 10:05:36.194412+00', '2026-10-20 10:20:36.194412+00', 'f0594651b94ef71b9e81b84a7cd280a5cec4e6e6c339983de162bbf191d09b16', NULL, 'c36710b4-f7bb-48c5-bf64-bc1a7aedb683');
INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('4c2b9a84-9140-4305-b367-b790aaefbccb', '2026-10-20 15:44:13.068541+00', '2026-10-20 15:59:13.068541+00', '14b71610bfd361df653a4f8e56a3c20ff79a0a5c815764176dcf9db007235349', NULL, '14118101-2b08-4d10-8f5a-df8e6b902ec7');
INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('a170c4a1-4bb8-4e38-8d72-bb7b34ec88bd', '2026-10-28 00:14:28.555253+00', '2026-10-28 00:29:28.554747+00', 'e59482ba342c1fc8face587738e2965330b9063a28e4f5484b87eb3fb2e59d15', NULL, 'ad24a93a-4c47-4c13-b329-35eb423ca65f');
INSERT INTO public.email_verification_tokens (id, created_at, expires_at, token_hash, used_at, user_id) VALUES ('d159f018-7036-426a-8a77-370c69cdba17', '2026-11-13 11:15:09.470607+00', '2026-11-13 11:30:09.470607+00', 'e2573b3ebf1d69672758a608958082bd6b3c5b12e9a52af58ddeb4d6d9b20a72', '2026-11-13 11:16:11.206627+00', '7b029d9a-4246-4273-be13-7a53ae121011');


--
-- Data for Name: invalidated_token; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000001', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/C64H0h3qiRc', NULL, '2026-10-29 16:41:58.241903+00', 'Buổi 1: Tổng ôn Hàm số & Khảo sát sự biến thiên', '11111111-aaaa-bbbb-cccc-000000000001', NULL, 1, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000002', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/W_8aEs16RI0', NULL, '2026-10-31 16:41:58.241903+00', 'Buổi 2: Phương pháp giải nhanh Mũ - Logarithm', '11111111-aaaa-bbbb-cccc-000000000001', NULL, 2, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000004', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/9SGDpanrc8U', NULL, '2026-10-29 16:41:58.241903+00', 'Buổi 1: Thiết kế kiến trúc Spring Boot 3 & RESTful API', '11111111-aaaa-bbbb-cccc-000000000002', NULL, 1, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000005', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/eIrMbAQSU34', NULL, '2026-10-31 16:41:58.241903+00', 'Buổi 2: Tích hợp Spring Security 6 & Nimbus JOSE JWT', '11111111-aaaa-bbbb-cccc-000000000002', NULL, 2, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000006', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/SqcY0GlETPk', NULL, '2026-11-02 16:41:58.241903+00', 'Buổi 3: Xây dựng Giao diện React Vite & Kết nối API', '11111111-aaaa-bbbb-cccc-000000000002', NULL, 3, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000007', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/IOfEQjlKH64', NULL, '2026-10-29 16:41:58.241903+00', 'Buổi 1: Chuẩn hóa phát âm IPA & Ngữ điệu tự nhiên', '11111111-aaaa-bbbb-cccc-000000000003', NULL, 1, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000008', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/nJJyilEPwpk', NULL, '2026-10-31 16:41:58.241903+00', 'Buổi 2: Chiến lược trả lời IELTS Speaking Part 1 & 2', '11111111-aaaa-bbbb-cccc-000000000003', NULL, 2, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000009', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/embed/N0RxRl9allI', NULL, '2026-11-02 16:41:58.241903+00', 'Buổi 3: Bẻ khóa dạng bài IELTS Reading & Skimming/Scanning', '11111111-aaaa-bbbb-cccc-000000000003', NULL, 3, '2026-10-28 16:41:58.241903+00');
INSERT INTO public.lessons (id, created_at, media_url, meeting_url, starts_at, title, course_id, ends_at, lesson_order, updated_at) VALUES ('22222222-aaaa-bbbb-cccc-000000000003', '2026-10-28 16:41:58.241903+00', 'https://www.youtube.com/watch?v=-wS9MsSwk6M', NULL, '2026-11-02 16:41:58.241903+00', 'Buổi 3: Hình học không gian Oxyz và Tọa độ hóa', '11111111-aaaa-bbbb-cccc-000000000001', NULL, 3, '2026-11-08 09:15:07.332185+00');


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.messages (id, conversation_id, sender_id, content, created_at, updated_at) VALUES ('dc7b96ef-f014-4dc1-9147-a702fb6e9242', '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', '7b029d9a-4246-4273-be13-7a53ae121011', 'hi', '2026-11-13 11:32:28.15501+00', '2026-11-13 11:32:28.15501+00');
INSERT INTO public.messages (id, conversation_id, sender_id, content, created_at, updated_at) VALUES ('a7543f3c-818b-479d-8fc3-ecce4b77f048', '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'hello', '2026-11-13 11:32:37.989687+00', '2026-11-13 11:32:37.989687+00');
INSERT INTO public.messages (id, conversation_id, sender_id, content, created_at, updated_at) VALUES ('b774670b-6875-4835-b2bb-c8b3430d7b98', '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', '7b029d9a-4246-4273-be13-7a53ae121011', 'chao co a', '2026-11-14 03:29:28.54554+00', '2026-11-14 03:29:28.54554+00');
INSERT INTO public.messages (id, conversation_id, sender_id, content, created_at, updated_at) VALUES ('cc16bee9-cdb7-47a3-9cc4-67d3b4e7846d', '52a2e0ed-e2ec-48be-bcb8-f8b11272a350', '63d91ca1-d606-4d51-849c-c90749a7bd5c', 'chao e', '2026-11-14 03:29:42.352294+00', '2026-11-14 03:29:42.352294+00');


--
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: payment_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('7c85191f-bfb2-4f34-96a9-a6e8f2abbb8a', 5000000, 5051000, '2026-11-13 11:30:30.647366+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, '2026-11-13 11:30:30.712967+00', 'MOCK', 'MOCK-8c3ee3f4-b1ca-4f0a-92c3-3eddfd7f504a', 'd01edcf7-7d11-43cc-ad94-502b89c583bf', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('1c66dc81-df75-47d8-9ea2-1fd7fc07da2a', 699000, 4352000, '2026-11-13 11:30:41.192462+00', 'Giữ tiền thanh toán cho enrollment ce43efe4-ee0a-4f63-a62d-de16b404bf0d', 'ESCROW_HOLD', NULL, NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'e3a93e29-97dc-49e6-a796-3875c5502996', 'SUCCESS', 'ce43efe4-ee0a-4f63-a62d-de16b404bf0d', NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('29d67e32-a9c4-4bd9-8188-2029a168587e', 200000, 200000, '2026-11-09 16:13:46.476117+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, '945f13fa-c531-430d-8994-b2e6e6c2e1bd', NULL, '2026-11-09 16:13:46.54041+00', 'MOCK', 'MOCK-99aeb841-7a0c-4126-bb9c-7a536c931a8c', '46854a39-17cd-4b70-bcce-2f43dad53b6d', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('756b69fd-b6f1-4745-8b54-4eaa0ee5983b', 150000, 4502000, '2026-11-13 11:31:40.146493+00', 'Hoàn tiền khiếu nại thành công', 'REFUND', '0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', 'a4038ab7-5e8c-47f9-ab65-df74b74ccff8', NULL, NULL, NULL, '6fcd78f7-719b-4b7c-8a5b-2f90e9a3c819', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('47c9dc38-807f-493a-a5dd-4364938f2b49', 500000, 500000, '2026-11-09 16:13:46.6157+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, '6f4fb7eb-718f-42be-a9c9-a9792ee067d8', NULL, '2026-11-09 16:13:46.675366+00', 'MOCK', 'MOCK-ecc7ad81-3eba-4921-8a0b-4c8211646707', '2dbd42c6-b2ae-4e4d-8420-ac18f8b256f0', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('14d2066a-03ed-40af-9692-a3fa83c26c14', 5000000, 5000000, '2026-11-13 11:33:37.306556+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, 'a4038ab7-5e8c-47f9-ab65-df74b74ccff8', NULL, '2026-11-13 11:33:37.482923+00', 'MOCK', 'MOCK-82d37c96-8c25-4474-b197-627d821073ca', 'cf1757e9-c797-45ae-9852-af933d51bf7e', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('e15fc294-51ec-4f45-81b6-6b961a04201c', 300000, 4202000, '2026-11-13 11:34:23.444265+00', 'Giữ tiền thanh toán cho booking 1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'ESCROW_HOLD', '1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'fa27a670-4a82-45e0-b323-00087fc386b3', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('32e8e1a8-5aa2-47c5-a93c-b3f14833ae52', 300000, 3902000, '2026-11-13 11:40:52.761328+00', 'Giữ tiền thanh toán cho booking 090411b7-8f84-4dc5-aced-2189dcc266f0', 'ESCROW_HOLD', '090411b7-8f84-4dc5-aced-2189dcc266f0', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '76d5cd5b-2bdd-4e79-aa09-453f39bb7b57', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('cdcd1bb0-2fc7-4512-be89-4e360dbd0978', 500000, 500000, '2026-11-13 11:16:27.081942+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, '2026-11-13 11:16:27.348711+00', 'MOCK', 'MOCK-62cde28a-96ea-4366-844e-9a11d36edeab', 'cd9329eb-bf11-4818-aac0-c1dcff68d9d4', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('6490dffb-f0f6-46fa-a5b4-d34f0b246f1a', 150000, 350000, '2026-11-13 11:18:49.069638+00', 'Giữ tiền thanh toán cho booking 0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', 'ESCROW_HOLD', '0ba45513-d9a5-4bc8-bc18-3c775a80dfcc', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'd0e21256-563d-4533-aaae-b290c67b7140', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('4658170b-7a8f-4cd1-920a-92467a71d0c3', 0, 5000000, '2026-11-13 11:54:47.540175+00', 'Giải ngân thu nhập gia sư cho booking 1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'ESCROW_RELEASE', '1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'a4038ab7-5e8c-47f9-ab65-df74b74ccff8', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'afb0d2e3-9baf-43e9-b5da-fa71d81ab714', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('455f7c52-4e88-4e9e-8fab-2520cf27a005', 200000, 550000, '2026-11-13 11:26:50.867276+00', 'Nạp tiền vào ví', 'DEPOSIT', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, '2026-11-13 11:26:50.901548+00', 'MOCK', 'MOCK-dd41334a-5bf9-4a08-a396-37ee0025951c', 'e9688642-5640-4af8-bea4-d4b0a9bd4551', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('628a234a-2c10-4671-9f48-ad0d2cf2731d', 499000, 51000, '2026-11-13 11:26:50.955788+00', 'Giữ tiền thanh toán cho enrollment ca2cf1fc-9a26-4f3a-a354-624f2d860685', 'ESCROW_HOLD', NULL, NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'd7f62873-7b92-4503-bb15-684c02566f20', 'SUCCESS', 'ca2cf1fc-9a26-4f3a-a354-624f2d860685', NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('5d1e8de3-bf42-4a04-801c-b192c4bdf63d', 300000, 300000, '2026-11-13 11:54:47.54071+00', 'Thu phí nền tảng cho booking 1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'ESCROW_RELEASE', '1c883ecc-e5d7-47fe-b5ca-c7e1aaa69fc5', 'd3976a7b-a0a4-4492-a95c-7fda56da9fe9', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '2de9ea27-f687-45be-a24f-c2d32e1c5b27', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('987b6f8f-addf-4927-a5ff-236726ee060f', 0, 5000000, '2026-11-13 11:54:56.068865+00', 'Giải ngân thu nhập gia sư cho booking 090411b7-8f84-4dc5-aced-2189dcc266f0', 'ESCROW_RELEASE', '090411b7-8f84-4dc5-aced-2189dcc266f0', 'a4038ab7-5e8c-47f9-ab65-df74b74ccff8', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '7d6aa991-7a86-49ee-9ede-1a6fbcd5b08a', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('8623276c-4409-49ce-8d81-10786e73a6b1', 300000, 600000, '2026-11-13 11:54:56.068865+00', 'Thu phí nền tảng cho booking 090411b7-8f84-4dc5-aced-2189dcc266f0', 'ESCROW_RELEASE', '090411b7-8f84-4dc5-aced-2189dcc266f0', 'd3976a7b-a0a4-4492-a95c-7fda56da9fe9', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '7715175a-b131-4a0e-926d-088899d38f0a', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('8f3cba69-eb31-4501-bd3d-0fbcbf3b5624', 2600000, 1302000, '2026-11-13 11:57:58.24935+00', 'Giữ tiền thanh toán cho booking 7c9cdc9d-ddfd-4265-987f-a10848375b90', 'ESCROW_HOLD', '7c9cdc9d-ddfd-4265-987f-a10848375b90', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'd32efb09-ce37-46d1-aa8c-8f9f886e67c2', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('e9b111dc-e9ea-4ba5-8bd8-aa9879572995', 2340000, 7340000, '2026-11-13 11:58:09.014782+00', 'Giải ngân thu nhập gia sư cho booking 7c9cdc9d-ddfd-4265-987f-a10848375b90', 'ESCROW_RELEASE', '7c9cdc9d-ddfd-4265-987f-a10848375b90', 'a4038ab7-5e8c-47f9-ab65-df74b74ccff8', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'a8bb766f-d9b2-4bf3-bc00-8963e16b266c', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('90f9d607-390a-4b97-8b75-ee1e56d9db83', 260000, 860000, '2026-11-13 11:58:09.014782+00', 'Thu phí nền tảng cho booking 7c9cdc9d-ddfd-4265-987f-a10848375b90', 'ESCROW_RELEASE', '7c9cdc9d-ddfd-4265-987f-a10848375b90', 'd3976a7b-a0a4-4492-a95c-7fda56da9fe9', 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '877e6cf0-61a1-414b-8580-99310cfb70a5', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('a9b3c6cc-0643-423e-9aa6-56da06e4171c', 450000, 852000, '2026-11-13 11:58:53.924811+00', 'Giữ tiền thanh toán cho booking 30cbaac5-a36c-4782-a193-e50b2374d42e', 'ESCROW_HOLD', '30cbaac5-a36c-4782-a193-e50b2374d42e', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, '0493829b-9ace-4b3e-91b3-3166a410dfed', 'SUCCESS', NULL, NULL);
INSERT INTO public.payment_transactions (id, amount, balance_after, created_at, note, type, booking_id, destination_wallet_id, source_wallet_id, updated_at, provider, provider_reference, reference_code, status, enrollment_id, withdrawal_id) VALUES ('844d1de9-0963-43f6-881f-2582bd79c070', 450000, 402000, '2026-11-14 03:28:37.970348+00', 'Giữ tiền thanh toán cho booking bb09b285-34e6-4147-88c9-0dfffa387018', 'ESCROW_HOLD', 'bb09b285-34e6-4147-88c9-0dfffa387018', NULL, 'bfd0f0a8-7555-48f6-b361-33a922d3124a', NULL, NULL, NULL, 'ac0325ee-7d33-4180-9650-2cc12f601679', 'SUCCESS', NULL, NULL);


--
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: tutor_applications; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tutor_applications (id, admin_note, bio, created_at, cv_url, status, subjects_can_teach, test_submission, update_at, user_id, updated_at) VALUES ('4c912c9f-51fb-4270-bf2a-447f56b32b19', NULL, 'Sinh viên năm cuối ĐH Sư Phạm, 2 năm kinh nghiệm dạy kèm Toán', '2026-10-20 04:22:33.774965+00', 'https://example.com/my-cv.pdf', 'APPROVED', 'Toán lớp 10, 11, 12', 'https://example.com/test-result.pdf', '2026-10-20 04:26:55.328392+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c', NULL);


--
-- Data for Name: tutor_profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tutor_profiles (user_id, bio, created_at, rating, subjects_can_teach, updated_at, available_schedule) VALUES ('63d91ca1-d606-4d51-849c-c90749a7bd5c', 'Sinh viên năm cuối ĐH Sư Phạm, 2 năm kinh nghiệm dạy kèm LÝ', '2026-10-20 04:26:55.346703+00', 5.00, 'LÝ lớp 10, 11, 12', '2026-11-08 09:10:05.762655+00', 'CÁC TỐI TRONG TUẦN');


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('63d91ca1-d606-4d51-849c-c90749a7bd5c', NULL, '2026-10-20 03:45:15.696106+00', 'user001', 'nguyengiang200x@gmail.com', '2026-10-20 03:52:22.726042+00', '$2a$10$lrhVg8yHAtB216GVaY4G3.CVUxm5FhD1H3qFQ4JCRr11DMfwz/OC2', NULL, 'TUTOR', 'ACTIVE', '2026-10-20 03:45:15.696106+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('36b8d9fd-9dfe-45ed-b4f1-7f068da1e033', NULL, '2026-10-20 09:56:34.467285+00', 'user002', 'nguyengiang2006x@gmail.com', '2026-10-28 17:02:03.080926+00', '$2a$10$171GJ9E/6KlrLU5Ovsntxe2s/BuJTJZ.0iClwfBM9QAAfP3g5MHCO', NULL, 'STUDENT', 'ACTIVE', '2026-10-20 09:56:34.467285+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('c36710b4-f7bb-48c5-bf64-bc1a7aedb683', NULL, '2026-10-20 10:05:36.113889+00', 'user003', 'nguyengiang2007x@gmail.com', '2026-10-28 17:02:15.295779+00', '$2a$10$iJhjr6PavSy08ezqd4HT/uQDXpwAYqZQQH8dnIa8OBTBTQPXjKHWa', NULL, 'STUDENT', 'ACTIVE', '2026-10-20 10:05:36.113889+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('14118101-2b08-4d10-8f5a-df8e6b902ec7', NULL, '2026-10-20 15:44:12.950452+00', 'user004', 'nguyengiang2010x@gmail.com', '2026-10-28 17:02:15.295779+00', '$2a$10$B79sT99sT/2qiBOIAfr57.ClUVAqfjH4AIGZOkemWuBKBMoxh4XJu', NULL, 'STUDENT', 'ACTIVE', '2026-10-20 15:44:12.950452+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('ad24a93a-4c47-4c13-b329-35eb423ca65f', NULL, '2026-10-28 00:14:28.48722+00', 'abc', 'anhembadao299@gmail.com', '2026-10-28 17:02:15.295779+00', '$2a$10$Vow30CyVnvMCYclf5Ki8L.C.t.fa5eBrsn/X9Ag.d2ZQMx5iHqeqi', NULL, 'STUDENT', 'ACTIVE', '2026-10-28 00:14:28.48722+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('a0000000-0000-0000-0000-000000000001', NULL, '2026-10-29 07:52:32.348089+00', 'Admin', 'admin@gmail.com', '2026-10-29 07:52:32.348089+00', '$2a$10$lrhVg8yHAtB216GVaY4G3.CVUxm5FhD1H3qFQ4JCRr11DMfwz/OC2', NULL, 'ADMIN', 'ACTIVE', '2026-10-29 07:52:32.348089+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('7b029d9a-4246-4273-be13-7a53ae121011', NULL, '2026-11-13 11:15:09.442733+00', 'Ngô Minh Quang', 'ngominhquang200x@gmail.com', '2026-11-13 11:16:11.202937+00', '$2a$10$ZZcI8SipMz6R58r69ZUYnOAY9SWPbG8o/jFDAQcmHhqlenEKPbqOK', NULL, 'STUDENT', 'ACTIVE', '2026-11-13 11:16:11.207621+00', NULL);
INSERT INTO public.users (id, avatar_url, created_at, display_name, email, email_verified_at, password_hash, phone, role, status, updated_at, zalo_link) VALUES ('1d1b2346-092c-4529-a34a-0c84a77a530d', NULL, '2026-11-04 09:03:59.365967+00', 'admin', 'admin123@gmail.com', '2026-11-04 09:03:59.34826+00', '$2a$10$smc/KSLMKBY.b1H3C2B8QOgEhfcpdQ8.XwGrP6LpghU29UIgTRkEq', NULL, 'ADMIN', 'ACTIVE', '2026-11-04 09:03:59.365967+00', NULL);


--
-- Data for Name: wallets; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('3c0c3583-56f0-4cc7-98ed-573e0d49e954', 0, '2026-10-20 10:05:36.192254+00', 0, 'NORMAL', '2026-10-20 10:05:36.192254+00', 'c36710b4-f7bb-48c5-bf64-bc1a7aedb683');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('abd1f1b9-5f53-48f1-99c2-0d3ca45cc452', 0, '2026-10-20 15:44:13.063833+00', 0, 'NORMAL', '2026-10-20 15:44:13.063833+00', '14118101-2b08-4d10-8f5a-df8e6b902ec7');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('3d93de52-78c9-43b3-8d69-bb8f8bf36aa3', 0, '2026-11-09 16:11:21.48097+00', 0, 'NORMAL', '2026-11-09 16:11:21.48097+00', 'ad24a93a-4c47-4c13-b329-35eb423ca65f');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('4d36a639-94e0-493b-9e45-1b1df6f34eb9', 0, '2026-11-09 16:11:21.48097+00', 0, 'NORMAL', '2026-11-09 16:11:21.48097+00', '1d1b2346-092c-4529-a34a-0c84a77a530d');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('945f13fa-c531-430d-8994-b2e6e6c2e1bd', 200000, '2026-11-09 16:11:21.48097+00', 0, 'NORMAL', '2026-11-09 16:13:46.54041+00', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('6f4fb7eb-718f-42be-a9c9-a9792ee067d8', 500000, '2026-10-20 09:56:34.529276+00', 0, 'NORMAL', '2026-11-09 16:13:46.675972+00', '36b8d9fd-9dfe-45ed-b4f1-7f068da1e033');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('a4038ab7-5e8c-47f9-ab65-df74b74ccff8', 7340000, '2026-10-20 03:45:15.702011+00', 0, 'NORMAL', '2026-11-13 11:58:09.021309+00', '63d91ca1-d606-4d51-849c-c90749a7bd5c');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('d3976a7b-a0a4-4492-a95c-7fda56da9fe9', 860000, '2026-11-13 11:37:39.835079+00', 0, 'PLATFORM', '2026-11-13 11:58:09.021309+00', 'a0000000-0000-0000-0000-000000000001');
INSERT INTO public.wallets (id, available_balance, created_at, holding_balance, type, updated_at, user_id) VALUES ('bfd0f0a8-7555-48f6-b361-33a922d3124a', 402000, '2026-11-13 11:16:26.955254+00', 2098000, 'NORMAL', '2026-11-14 03:28:37.976373+00', '7b029d9a-4246-4273-be13-7a53ae121011');


--
-- Data for Name: wishlist_items; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: withdrawal; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: action_logs action_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.action_logs
    ADD CONSTRAINT action_logs_pkey PRIMARY KEY (id);


--
-- Name: articles articles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.articles
    ADD CONSTRAINT articles_pkey PRIMARY KEY (id);


--
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);


--
-- Name: brokerage_packages brokerage_packages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.brokerage_packages
    ADD CONSTRAINT brokerage_packages_pkey PRIMARY KEY (id);


--
-- Name: cart_items cart_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: conversations conversations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT conversations_pkey PRIMARY KEY (id);


--
-- Name: course_enrollments course_enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT course_enrollments_pkey PRIMARY KEY (id);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- Name: disputes disputes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT disputes_pkey PRIMARY KEY (id);


--
-- Name: email_verification_tokens email_verification_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_verification_tokens
    ADD CONSTRAINT email_verification_tokens_pkey PRIMARY KEY (id);


--
-- Name: invalidated_token invalidated_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invalidated_token
    ADD CONSTRAINT invalidated_token_pkey PRIMARY KEY (id);


--
-- Name: lessons lessons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT lessons_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: payment_method payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (id);


--
-- Name: payment_transactions payment_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT payment_transactions_pkey PRIMARY KEY (id);


--
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- Name: tutor_applications tutor_applications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tutor_applications
    ADD CONSTRAINT tutor_applications_pkey PRIMARY KEY (id);


--
-- Name: tutor_profiles tutor_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tutor_profiles
    ADD CONSTRAINT tutor_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: reviews uk3p9j9vyr1qofbcxju65es206r; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT uk3p9j9vyr1qofbcxju65es206r UNIQUE (booking_id);


--
-- Name: wallets uk59hs4d6rv3ay5g42ny3ilxms0; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wallets
    ADD CONSTRAINT uk59hs4d6rv3ay5g42ny3ilxms0 UNIQUE (user_id, type);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: payment_method uk9p2fk6b9gbuq4o4k3wrcvl7cp; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT uk9p2fk6b9gbuq4o4k3wrcvl7cp UNIQUE (user_id, account_number);


--
-- Name: cart_items uk_cart_user_course; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT uk_cart_user_course UNIQUE (user_id, course_id);


--
-- Name: wishlist_items uk_wishlist_user_course; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wishlist_items
    ADD CONSTRAINT uk_wishlist_user_course UNIQUE (user_id, course_id);


--
-- Name: email_verification_tokens ukah12jdrm74nfnmu11x9xxlbex; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_verification_tokens
    ADD CONSTRAINT ukah12jdrm74nfnmu11x9xxlbex UNIQUE (token_hash);


--
-- Name: payment_transactions ukb5pcsf1agn8v5asy2r569hylw; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT ukb5pcsf1agn8v5asy2r569hylw UNIQUE (withdrawal_id);


--
-- Name: tutor_applications ukhy3ouqbnq0w6qfqg5u0frp88l; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tutor_applications
    ADD CONSTRAINT ukhy3ouqbnq0w6qfqg5u0frp88l UNIQUE (user_id, status);


--
-- Name: course_enrollments ukrjq0vqhmyin2pyjo9rnl8ykl1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT ukrjq0vqhmyin2pyjo9rnl8ykl1 UNIQUE (student_id, course_id);


--
-- Name: reviews ukrq8aj69p6qdhxdt983bheuhdi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT ukrq8aj69p6qdhxdt983bheuhdi UNIQUE (enrollment_id);


--
-- Name: payment_transactions ukt1i11uyqwk9gwl986p5kr010p; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT ukt1i11uyqwk9gwl986p5kr010p UNIQUE (reference_code);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: wallets wallets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wallets
    ADD CONSTRAINT wallets_pkey PRIMARY KEY (id);


--
-- Name: wishlist_items wishlist_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wishlist_items
    ADD CONSTRAINT wishlist_items_pkey PRIMARY KEY (id);


--
-- Name: withdrawal withdrawal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.withdrawal
    ADD CONSTRAINT withdrawal_pkey PRIMARY KEY (id);


--
-- Name: idx2bdo6lu7jvkv8ke1mrnarqv9q; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx2bdo6lu7jvkv8ke1mrnarqv9q ON public.comments USING btree (parent_id, article_id);


--
-- Name: idx2jo41j3vk3i6v0lrh9mq61jjr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx2jo41j3vk3i6v0lrh9mq61jjr ON public.action_logs USING btree (target_id);


--
-- Name: idx_action_log_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_action_log_created_at ON public.action_logs USING btree (created_at);


--
-- Name: idx_action_log_target; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_action_log_target ON public.action_logs USING btree (target_type, target_id);


--
-- Name: idx_action_log_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_action_log_user ON public.action_logs USING btree (user_id);


--
-- Name: idx_cart_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_cart_user ON public.cart_items USING btree (user_id);


--
-- Name: idx_conversations_student_updated; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversations_student_updated ON public.conversations USING btree (student_id, updated_at);


--
-- Name: idx_conversations_tutor_updated; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversations_tutor_updated ON public.conversations USING btree (tutor_id, updated_at);


--
-- Name: idx_messages_conversation_created; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_conversation_created ON public.messages USING btree (conversation_id, created_at);


--
-- Name: idx_reviews_reviewer_created; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reviews_reviewer_created ON public.reviews USING btree (reviewer_id, created_at);


--
-- Name: idx_reviews_tutor_created; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reviews_tutor_created ON public.reviews USING btree (tutor_id, created_at);


--
-- Name: idx_wishlist_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_wishlist_user ON public.wishlist_items USING btree (user_id);


--
-- Name: idxieie5egigg2fh0pqvxihaayuy; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxieie5egigg2fh0pqvxihaayuy ON public.comments USING btree (article_id);


--
-- Name: idxk8oxftj2b4s3wlip1768dv4a; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxk8oxftj2b4s3wlip1768dv4a ON public.action_logs USING btree (created_at);


--
-- Name: idxl2kyy1r86epv8gdaxfifv1eq0; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxl2kyy1r86epv8gdaxfifv1eq0 ON public.action_logs USING btree (target_user_id);


--
-- Name: cart_items cart_items_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id) ON DELETE CASCADE;


--
-- Name: cart_items cart_items_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: lessons fk17ucc7gjfjddsyi0gvstkqeat; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fk17ucc7gjfjddsyi0gvstkqeat FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: withdrawal fk21nf4qhjccx8nwq2sqjkvkpgx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.withdrawal
    ADD CONSTRAINT fk21nf4qhjccx8nwq2sqjkvkpgx FOREIGN KEY (wallet_id) REFERENCES public.wallets(id);


--
-- Name: conversations fk281qnh5jigvg90uiwrr865pbg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk281qnh5jigvg90uiwrr865pbg FOREIGN KEY (tutor_id) REFERENCES public.users(id);


--
-- Name: reviews fk28an517hrxtt2bsg93uefugrm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk28an517hrxtt2bsg93uefugrm FOREIGN KEY (booking_id) REFERENCES public.bookings(id);


--
-- Name: payment_transactions fk28gy0sw29lui429wk3be747t6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT fk28gy0sw29lui429wk3be747t6 FOREIGN KEY (enrollment_id) REFERENCES public.course_enrollments(id);


--
-- Name: bookings fk3gcbjcudssgkxgwhkauoy5u4m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk3gcbjcudssgkxgwhkauoy5u4m FOREIGN KEY (package_id) REFERENCES public.brokerage_packages(id);


--
-- Name: tutor_profiles fk3h4hc71bbx1xyce6kbjm1bagn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tutor_profiles
    ADD CONSTRAINT fk3h4hc71bbx1xyce6kbjm1bagn FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: conversations fk3tsdw1joa61a2a7cbrxq9w8g6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk3tsdw1joa61a2a7cbrxq9w8g6 FOREIGN KEY (student_id) REFERENCES public.users(id);


--
-- Name: disputes fk4wgji1pxtiyvvsg3ft92tjl6v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT fk4wgji1pxtiyvvsg3ft92tjl6v FOREIGN KEY (enrollment_id) REFERENCES public.course_enrollments(id);


--
-- Name: bookings fk64axe7rxk1w0yybe1w9wwevv9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk64axe7rxk1w0yybe1w9wwevv9 FOREIGN KEY (conversation_id) REFERENCES public.conversations(id);


--
-- Name: action_logs fk6locj4o5mo2ktklow0ywpfheg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.action_logs
    ADD CONSTRAINT fk6locj4o5mo2ktklow0ywpfheg FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: disputes fk70i5mk4tv7uk491cadn733q0q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT fk70i5mk4tv7uk491cadn733q0q FOREIGN KEY (created_by_id) REFERENCES public.users(id);


--
-- Name: brokerage_packages fk89m851ye2278em1r1uvhrejty; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.brokerage_packages
    ADD CONSTRAINT fk89m851ye2278em1r1uvhrejty FOREIGN KEY (tutor_id) REFERENCES public.users(id);


--
-- Name: comments fk8omq0tc18jd43bu5tjh6jvraq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: tutor_applications fk8uwa99u5cnpwbmpp1ccon2qgt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tutor_applications
    ADD CONSTRAINT fk8uwa99u5cnpwbmpp1ccon2qgt FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: action_logs fk8vramvvinxfmhna00oohk14cu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.action_logs
    ADD CONSTRAINT fk8vramvvinxfmhna00oohk14cu FOREIGN KEY (target_user_id) REFERENCES public.users(id);


--
-- Name: action_logs fka0tll6fnhj3pla79m4t4r7ak5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.action_logs
    ADD CONSTRAINT fka0tll6fnhj3pla79m4t4r7ak5 FOREIGN KEY (admin_id) REFERENCES public.users(id);


--
-- Name: bookings fkar27os7sd4oj16rjdimbedgtf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fkar27os7sd4oj16rjdimbedgtf FOREIGN KEY (student_id) REFERENCES public.users(id);


--
-- Name: wallets fkc1foyisidw7wqqrkamafuwn4e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wallets
    ADD CONSTRAINT fkc1foyisidw7wqqrkamafuwn4e FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: reviews fkd1isgfajhtdl8mgg29up6mofi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fkd1isgfajhtdl8mgg29up6mofi FOREIGN KEY (reviewer_id) REFERENCES public.users(id);


--
-- Name: articles fke02fs2ut6qqoabfhj325wcjul; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.articles
    ADD CONSTRAINT fke02fs2ut6qqoabfhj325wcjul FOREIGN KEY (author_id) REFERENCES public.users(id);


--
-- Name: withdrawal fke0lf36eoctbkgxwyqc5lo5wbj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.withdrawal
    ADD CONSTRAINT fke0lf36eoctbkgxwyqc5lo5wbj FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: course_enrollments fkf78cq7ecdpk1clt1w5ofnb34t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT fkf78cq7ecdpk1clt1w5ofnb34t FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: reviews fkgcmnyjacr5dtpqf69kj8c4mtr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fkgcmnyjacr5dtpqf69kj8c4mtr FOREIGN KEY (enrollment_id) REFERENCES public.course_enrollments(id);


--
-- Name: payment_transactions fkgruucsxmibdjhdldfywe39cxk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT fkgruucsxmibdjhdldfywe39cxk FOREIGN KEY (source_wallet_id) REFERENCES public.wallets(id);


--
-- Name: bookings fki0lvs0c2wc4o2wyfi8bf0lwls; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fki0lvs0c2wc4o2wyfi8bf0lwls FOREIGN KEY (tutor_id) REFERENCES public.users(id);


--
-- Name: email_verification_tokens fki1c4mmamlb8keqt74k4lrtwhc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_verification_tokens
    ADD CONSTRAINT fki1c4mmamlb8keqt74k4lrtwhc FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: comments fkk4ib6syde10dalk7r7xdl0m5p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fkk4ib6syde10dalk7r7xdl0m5p FOREIGN KEY (article_id) REFERENCES public.articles(id) ON DELETE CASCADE;


--
-- Name: payment_method fkkeh6a1awkqba7l425dq4bi0l3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT fkkeh6a1awkqba7l425dq4bi0l3 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: comments fklri30okf66phtcgbe5pok7cc0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fklri30okf66phtcgbe5pok7cc0 FOREIGN KEY (parent_id) REFERENCES public.comments(id) ON DELETE CASCADE;


--
-- Name: disputes fkmbmkglcru85cdq73ih2ko4m1k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT fkmbmkglcru85cdq73ih2ko4m1k FOREIGN KEY (booking_id) REFERENCES public.bookings(id);


--
-- Name: course_enrollments fkn0fy1fut1f7j4kxwxfl925qnd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT fkn0fy1fut1f7j4kxwxfl925qnd FOREIGN KEY (student_id) REFERENCES public.users(id);


--
-- Name: payment_transactions fkn3fs4986tsx5u32105n8q47wc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT fkn3fs4986tsx5u32105n8q47wc FOREIGN KEY (destination_wallet_id) REFERENCES public.wallets(id);


--
-- Name: reviews fknqd3rmo8lo3y5cncg277gvfiq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fknqd3rmo8lo3y5cncg277gvfiq FOREIGN KEY (tutor_id) REFERENCES public.users(id);


--
-- Name: payment_transactions fkoij4h3g5n6kn5g6743g7viepj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT fkoij4h3g5n6kn5g6743g7viepj FOREIGN KEY (booking_id) REFERENCES public.bookings(id);


--
-- Name: courses fkp39d96v9hcwwij5bx2jm4g69u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT fkp39d96v9hcwwij5bx2jm4g69u FOREIGN KEY (tutor_id) REFERENCES public.users(id);


--
-- Name: invalidated_token fkpv7k1rtxl6wjqu8o05oachfp1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invalidated_token
    ADD CONSTRAINT fkpv7k1rtxl6wjqu8o05oachfp1 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: payment_transactions fksh8qigpfv6e4c8opo01twjh5i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transactions
    ADD CONSTRAINT fksh8qigpfv6e4c8opo01twjh5i FOREIGN KEY (withdrawal_id) REFERENCES public.withdrawal(id);


--
-- Name: disputes fkt7x5p1o0nm10e9ikdlucbx1u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT fkt7x5p1o0nm10e9ikdlucbx1u FOREIGN KEY (student_id) REFERENCES public.users(id);


--
-- Name: messages messages_conversation_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_conversation_id_fkey FOREIGN KEY (conversation_id) REFERENCES public.conversations(id);


--
-- Name: messages messages_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id);


--
-- Name: wishlist_items wishlist_items_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wishlist_items
    ADD CONSTRAINT wishlist_items_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id) ON DELETE CASCADE;


--
-- Name: wishlist_items wishlist_items_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wishlist_items
    ADD CONSTRAINT wishlist_items_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--


