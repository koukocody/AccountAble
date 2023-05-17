import React from "react";

import Endpoint from "../Endpoint";
import ProductTypesContainer from "./ProductTypesContainer";
import {
  transformItemData,
  transformAccountsData,
  itemCategories,
  accountsCategories,
} from "../../dataUtilities";

const Items = () => (
  <>
    <ProductTypesContainer productType="">

    </ProductTypesContainer>
  </>
);

Items.displayName = "Items";

export default Items;
