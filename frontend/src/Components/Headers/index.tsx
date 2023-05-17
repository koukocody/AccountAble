import React, { useContext } from "react";
import Callout from "plaid-threads/Callout";
import Button from "plaid-threads/Button";
import InlineLink from "plaid-threads/InlineLink";

import Link from "../Link";
import Context from "../../Context";

import styles from "./index.module.scss";

const Header = () => {
  const {
    itemId,
    accessToken,
    linkToken,
    linkSuccess,
    isItemAccess,
    backend,
    linkTokenError,
    isPaymentInitiation,
  } = useContext(Context);

  return (
    <div className={styles.grid}>
      <h3 className={styles.title}>AccountAble</h3>

      {!linkSuccess ? (
        <>
          <h4 className={styles.subtitle}>
            Partnered with Plaid to bring you quick and secure connections to your financial institutions.
          </h4>
          <p className={styles.introPar}>
            To create a connection between AccountAble and your financial institution of choice, please click the button below. This will launch Plaid Link - a secure method of authorizing AccountAble access to your financial data. You choose what AccountAble can and can not see. This process will need to be repeated for every financial institution you wish to connect to AccountAble.
          </p>
          {/* message if backend is not running and there is no link token */}
          {!backend ? (
            <Callout warning>
              Unable to fetch link_token...
            </Callout>
          ) : /* message if backend is running and there is no link token */
          linkToken == null && backend ? (
            <Callout warning>
              <div>
                Unable to fetch link_token...
              </div>
              <div>
                Error Code: <code>{linkTokenError.error_code}</code>
              </div>
              <div>
                Error Type: <code>{linkTokenError.error_type}</code>{" "}
              </div>
              <div>Error Message: {linkTokenError.error_message}</div>
            </Callout>
          ) : linkToken === "" ? (
            <div className={styles.linkButton}>
              <Button large disabled>
                Loading...
              </Button>
            </div>
          ) : (
            <div className={styles.linkButton}>
              <Link />
            </div>
          )}
        </>
      ) : (
        <>
          {isPaymentInitiation ? (
            <>
            <h4 className={styles.subtitle}>
              Congrats! Your payment is now confirmed.
              <p/>
              <Callout>
                You can see information of all your payments in the{' '}
                <InlineLink
                    href="https://dashboard.plaid.com/activity/payments"
                    target="_blank"
                >
                  Payments Dashboard
                </InlineLink>
                .
              </Callout>
            </h4>
            <p className={styles.requests}>
              Now that the 'payment_id' stored in your server, you can use it to access the payment information:
            </p>
          </>
          ) : /* If not using the payment_initiation product, show the item_id and access_token information */ (
            <>
            {isItemAccess ? (
                <h4 className={styles.subtitle}>
                  Congrats! You have now linked an account!
                </h4>
            ) : (
                <h4 className={styles.subtitle}>
                  <Callout warning>
                    Unable to create an item. Please check your backend server
                  </Callout>
                </h4>
            )}
            {isItemAccess && (
                <p className={styles.requests}>
                    You may now close this webpage and return to the AccountAble app. If you need to connect to additional financial institutions, please repeat this process.
                </p>
            )}
          </>
          )}
        </>
      )}
    </div>
  );
};

Header.displayName = "Header";

export default Header;
