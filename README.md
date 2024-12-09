# Springboot - Đăng Quang Watch clone
### Technologies:
- HTML/CSS/JS.
- Springboot v3.3.2.
  - Spring JPA.
  - Spring Web.
  - Spring Mail.
  - Spring Security.
  - Lombok.
  - Thymeleaf.
  - Validation.
  - WebSocket.
- [Live reload with Tailwindcss](https://www.wimdeblauwe.com/blog/2022/08/27/thymeleaf-live-reload-with-spring-boot-and-tailwind-css/).
- [Cloudinary](https://console.cloudinary.com/).
- [VNPay Sandbox](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html).

### Database:
- Server type: MariaDB.
- Server version: 10.4.22-MariaDB - mariadb.org.
- Server charset: UTF-8 Unicode (utf8mb4).
- SQL file: `database.sql`

### Features:
- Fully responsive with Tailwindcss.
- User interface:
  - Login & Regsiter.
  - View products.
  - Search products.
  - Place order.
  - Track order.
- Admin panel:
  - Crud operations.
    - Products.
    - Accounts.
    - Orders.
  - Orders tracking.
  - Track orders.
  - Analytics.
  - Import & export tracking.
- Database triggers & procedures.

### Prequesition:
- Nodejs installed.
- Have suitable JDK version.
- Have Maven installed.
- Have a [Cloudinary](https://console.cloudinary.com/) developer account.
- Have a [VNPay Merchant](https://sandbox.vnpayment.vn/devreg/) account.
- Have an SMPT service.
- Have google auth credentials setup.
- Have github auth credentials setup.
- **(optional)** Have docker installed to run mail-dev
- Config Spring **application.properties**. All configurations are inside: `main/resources/application-properties.txt`
- Setup MySQL/MariaDB database from `database.sql`.

### Run & Build:
1. Start the Spring Boot application with the local profile. `mvn spring-boot:run`.
2. Run `npm run build && npm run watch` from the command line.
3. As a final change to the pom.xml, we can add a profile that calls our production NPM scripts. At release time, be sure to enable this Maven profile. `mvn spring-boot:run -P release`.
4. **(optional)** run mail-dev: `docker compose up -d` (If you don't have a SMPT service).

### Notes:
- Product page is not very polished.
- No product ratings.
- No phone number verification.
- UI design is not modern.

### Screenshots:
#### Auth ui:
- Login page:
![Login page](screenshots/login.png "Screenshot")
- Register page:
![Register page](screenshots/register.png "Screenshot")
#### User ui:
- Landing page:
![Landing page](screenshots/landing_page.png "Screenshot")
- Product page:
![Product page](screenshots/product_page.png "Screenshot")
- Cart:
![Cart](screenshots/cart.png "Screenshot")
- Track your orders:
![Orders page](screenshots/tracking_order.png "Screenshot")
- Profile:
![Profile page](screenshots/profile.png "Screenshot")
### Admin ui:
- Analytics:
![Analytics page](screenshots/admin_panel.png "Screenshot")
- Manage product (watches):
![Watch page](screenshots/manage_watchs.png "Screenshot")
#### Emails:
- Email confimation:
![Email confirmation](screenshots/email_confirmation.png "Screenshot")
- Order successful email:
![Order email](screenshots/email_order.png "Screenshot")

### Deploy maven with docker:
1. Build docker image: `docker build -t dangquangwatch .`
2. Run docker image: `docker run -p 8080:8080 dangquangwatch`
3. Access the application: `http://localhost:8080`

### Deploy maven with heroku:
1. Build docker image: `docker build -t dangquangwatch .`
2. Run docker image: `docker run -p 8080:8080 dangquangwatch`
3. Access the application: `http://localhost:8080`
4. Deploy to heroku: `heroku container:push web`
5. Access the application: `https://dangquangwatch.herokuapp.com`