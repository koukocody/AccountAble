# AccountAble

The Plaid quickstart documentation can be found here: [**quickstart guide**][quickstart].

## 1. Clone the repository

Using https:

```bash
git clone https://github.com/koukocody/AccountAble
```

#### Special instructions for Windows

Note - because this repository makes use of symbolic links, to run this on a Windows machine, make sure you have checked the "enable symbolic links" box when you download Git to your local machine. Then you can run the above commands to clone the program. Otherwise, you may open your Git Bash terminal as an administrator and use the following command when cloning the project

```bash
git clone -c core.symlinks=true https://github.com/koukocody/AccountAble
```

## 2. Set up your environment variables

```bash
cp .env.example .env
```

Copy `.env.example` to a new file called `.env` and fill out the environment variables inside. At
minimum `PLAID_CLIENT_ID` and `PLAID_SECRET` must be filled out. Get your Client ID and secrets from
the dashboard: https://dashboard.plaid.com/account/keys

> NOTE: `.env` files are a convenient local development tool. Never run a production application
> using an environment file with secrets in it.

## 3. Run the application

#### Pre-requisites

- Your environment variables populated in `.env`
- [npm](https://www.npmjs.com/get-npm)
- If using Windows, a command line utility capable of running basic Unix shell commands

#### Running the application

Run the start.sh script found in \AccountAble\java\start.sh


## Test credentials

In Sandbox, you can log in to any supported institution (except Capital One) using `user_good` as the username and `pass_good` as the password. If prompted to enter a 2-factor authentication code, enter `1234`.



[quickstart]: https://plaid.com/docs/quickstart

