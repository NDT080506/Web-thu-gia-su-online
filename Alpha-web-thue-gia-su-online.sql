CREATE TYPE "user_role" AS ENUM (
  'guest',
  'student',
  'tutor',
  'admin'
);

CREATE TYPE "account_status" AS ENUM (
  'active',
  'suspended',
  'banned'
);

CREATE TYPE "wallet_type" AS ENUM (
  'normal',
  'commercial',
  'platform'
);

CREATE TYPE "transaction_type" AS ENUM (
  'deposit',
  'payment',
  'escrow_hold',
  'escrow_release',
  'withdraw',
  'refund'
);

CREATE TABLE "users" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "email" varchar(255) UNIQUE NOT NULL,
  "password_hash" varchar(255) NOT NULL,
  "display_name" varchar(150) NOT NULL,
  "avatar_url" text,
  "role" user_role NOT NULL DEFAULT 'guest',
  "status" account_status NOT NULL DEFAULT 'active',
  "phone" varchar(20),
  "zalo_link" text,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "invalidated_token" (
  "id" varchar(255) PRIMARY KEY,
  "user_id" uuid NOT NULL,
  "expired_date" timestamptz NOT NULL
);

CREATE TABLE "payment_method" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "user_id" uuid NOT NULL,
  "bank_name" varchar(100),
  "account_number" varchar(50),
  "account_holder_name" varchar(150),
  "is_default" boolean DEFAULT false
);

CREATE TABLE "action_logs" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "admin_id" uuid,
  "target_user_id" uuid,
  "target_type" varchar(50),
  "target_id" uuid,
  "action_code" varchar(100),
  "old_value" jsonb,
  "new_value" jsonb,
  "reason" text,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "tutor_applications" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "user_id" uuid NOT NULL,
  "cv_url" text NOT NULL,
  "test_submission" text,
  "status" varchar(50) NOT NULL DEFAULT 'pending',
  "admin_note" text,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "tutor_profiles" (
  "user_id" uuid PRIMARY KEY,
  "bio" text,
  "subjects_can_teach" text,
  "rating" numeric(3,2) DEFAULT 5,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "brokerage_packages" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "name" varchar(255) NOT NULL,
  "price" bigint NOT NULL,
  "platform_fee_percent" int NOT NULL DEFAULT 10,
  "is_active" boolean DEFAULT true
);

CREATE TABLE "bookings" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "package_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "tutor_id" uuid,
  "subject" varchar(100),
  "status" varchar(50) NOT NULL DEFAULT 'pending_payment',
  "total_price" bigint NOT NULL,
  "platform_fee" bigint NOT NULL,
  "tutor_income" bigint NOT NULL,
  "contact_unlocked_at" timestamptz,
  "student_confirmed_at" timestamptz,
  "student_rating" int,
  "student_review" text,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now()),
  CHECK (total_price = platform_fee + tutor_income)
);

CREATE TABLE "messages" (
  "id" bigserial PRIMARY KEY,
  "booking_id" uuid NOT NULL,
  "sender_id" uuid NOT NULL,
  "content" text NOT NULL,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "courses" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tutor_id" uuid NOT NULL,
  "title" text NOT NULL,
  "price" bigint NOT NULL,
  "is_live" boolean DEFAULT false,
  "is_active" boolean DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "lessons" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "course_id" uuid NOT NULL,
  "title" varchar(255) NOT NULL,
  "media_url" varchar(500),
  "meeting_url" varchar(500),
  "starts_at" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "course_enrollments" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "student_id" uuid NOT NULL,
  "course_id" uuid NOT NULL,
  "price_paid" bigint NOT NULL,
  "status" varchar(50) NOT NULL DEFAULT 'active',
  "progress_percent" int DEFAULT 0,
  "completed_at" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "disputes" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "booking_id" uuid UNIQUE NOT NULL,
  "student_id" uuid NOT NULL,
  "reason" text NOT NULL,
  "status" varchar(50) NOT NULL DEFAULT 'pending',
  "refund_amount" bigint NOT NULL DEFAULT 0,
  "admin_note" text,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "resolved_at" timestamptz
);

CREATE TABLE "wallets" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "user_id" uuid,
  "type" wallet_type NOT NULL,
  "available_balance" bigint NOT NULL DEFAULT 0,
  "holding_balance" bigint NOT NULL DEFAULT 0,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now()),
  CHECK (available_balance >= 0 AND holding_balance >= 0),
  CHECK (type = 'commercial' OR holding_balance = 0)
);

CREATE TABLE "payment_transactions" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "source_wallet_id" uuid,
  "destination_wallet_id" uuid,
  "booking_id" uuid,
  "type" transaction_type NOT NULL,
  "amount" bigint NOT NULL,
  "balance_after" bigint NOT NULL,
  "note" text,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  CHECK (amount > 0)
);

CREATE TABLE "articles" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "author_id" uuid NOT NULL,
  "author_nickname" varchar(150),
  "author_avatar_url" varchar(500),
  "title" varchar(255) NOT NULL,
  "content" text NOT NULL,
  "min_role_level" int DEFAULT 0,
  "is_published" boolean DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "comments" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "article_id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "parent_id" uuid,
  "content" text NOT NULL,
  "is_hidden" boolean DEFAULT false,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE INDEX ON "invalidated_token" ("expired_date");

CREATE INDEX ON "invalidated_token" ("user_id");

CREATE UNIQUE INDEX ON "payment_method" ("user_id", "account_number");

CREATE INDEX ON "action_logs" ("target_user_id");

CREATE INDEX ON "action_logs" ("target_id");

CREATE INDEX ON "action_logs" ("created_at");

CREATE UNIQUE INDEX ON "tutor_applications" ("user_id", "status");

CREATE INDEX ON "messages" ("booking_id", "created_at");

CREATE UNIQUE INDEX ON "course_enrollments" ("student_id", "course_id");

CREATE UNIQUE INDEX ON "wallets" ("user_id", "type");

CREATE INDEX ON "comments" ("article_id");

CREATE INDEX ON "comments" ("parent_id", "article_id");

COMMENT ON COLUMN "users"."phone" IS 'Ẩn, chỉ lộ khi booking.active';

COMMENT ON COLUMN "action_logs"."target_type" IS 'booking, wallet, user...';

COMMENT ON COLUMN "brokerage_packages"."price" IS 'Tổng học phí';

COMMENT ON COLUMN "bookings"."student_rating" IS '1-5';

COMMENT ON COLUMN "wallets"."user_id" IS 'Null nếu platform';

COMMENT ON COLUMN "payment_transactions"."amount" IS 'LUÔN DƯƠNG';

COMMENT ON COLUMN "articles"."min_role_level" IS '0->3';

ALTER TABLE "invalidated_token" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_method" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "action_logs" ADD FOREIGN KEY ("admin_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "action_logs" ADD FOREIGN KEY ("target_user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tutor_applications" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tutor_profiles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "bookings" ADD FOREIGN KEY ("package_id") REFERENCES "brokerage_packages" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "bookings" ADD FOREIGN KEY ("student_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "bookings" ADD FOREIGN KEY ("tutor_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "messages" ADD FOREIGN KEY ("booking_id") REFERENCES "bookings" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "messages" ADD FOREIGN KEY ("sender_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "courses" ADD FOREIGN KEY ("tutor_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "lessons" ADD FOREIGN KEY ("course_id") REFERENCES "courses" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "course_enrollments" ADD FOREIGN KEY ("student_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "course_enrollments" ADD FOREIGN KEY ("course_id") REFERENCES "courses" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "disputes" ADD FOREIGN KEY ("booking_id") REFERENCES "bookings" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "disputes" ADD FOREIGN KEY ("student_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "wallets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_transactions" ADD FOREIGN KEY ("source_wallet_id") REFERENCES "wallets" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_transactions" ADD FOREIGN KEY ("destination_wallet_id") REFERENCES "wallets" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_transactions" ADD FOREIGN KEY ("booking_id") REFERENCES "bookings" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "articles" ADD FOREIGN KEY ("author_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "comments" ADD FOREIGN KEY ("article_id") REFERENCES "articles" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "comments" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "comments" ADD FOREIGN KEY ("parent_id") REFERENCES "comments" ("id") DEFERRABLE INITIALLY IMMEDIATE;
