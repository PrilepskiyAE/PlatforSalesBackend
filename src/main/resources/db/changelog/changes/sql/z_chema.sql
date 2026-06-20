-- pk_categories
ALTER TABLE IF EXISTS categories ADD CONSTRAINT pk_categories PRIMARY KEY (category_id);

-- pk_customer_customer_demo
ALTER TABLE IF EXISTS customer_customer_demo ADD CONSTRAINT pk_customer_customer_demo PRIMARY KEY (customer_id, customer_type_id);

-- pk_customer_demographics
ALTER TABLE IF EXISTS customer_demographics ADD CONSTRAINT pk_customer_demographics PRIMARY KEY (customer_type_id);

-- pk_customers
ALTER TABLE IF EXISTS customers ADD CONSTRAINT pk_customers PRIMARY KEY (customer_id);

-- pk_employees
ALTER TABLE IF EXISTS employees ADD CONSTRAINT pk_employees PRIMARY KEY (employee_id);

-- pk_employee_territories
ALTER TABLE IF EXISTS employee_territories ADD CONSTRAINT pk_employee_territories PRIMARY KEY (employee_id, territory_id);

-- pk_order_details
ALTER TABLE IF EXISTS order_details ADD CONSTRAINT pk_order_details PRIMARY KEY (order_id, product_id);

-- pk_orders
ALTER TABLE IF EXISTS orders ADD CONSTRAINT pk_orders PRIMARY KEY (order_id);

-- pk_products
ALTER TABLE IF EXISTS products ADD CONSTRAINT pk_products PRIMARY KEY (product_id);

-- pk_region
ALTER TABLE IF EXISTS region ADD CONSTRAINT pk_region PRIMARY KEY (region_id);

-- pk_shippers
ALTER TABLE IF EXISTS shippers ADD CONSTRAINT pk_shippers PRIMARY KEY (shipper_id);

-- pk_suppliers
ALTER TABLE IF EXISTS suppliers ADD CONSTRAINT pk_suppliers PRIMARY KEY (supplier_id);

-- pk_territories
ALTER TABLE IF EXISTS territories ADD CONSTRAINT pk_territories PRIMARY KEY (territory_id);

-- pk_usstates
ALTER TABLE IF EXISTS us_states ADD CONSTRAINT pk_usstates PRIMARY KEY (state_id);

-- fk_orders_customers
ALTER TABLE IF EXISTS orders ADD CONSTRAINT fk_orders_customers FOREIGN KEY (customer_id) REFERENCES customers(customer_id);

-- fk_orders_employees
ALTER TABLE IF EXISTS orders ADD CONSTRAINT fk_orders_employees FOREIGN KEY (employee_id) REFERENCES employees(employee_id);

-- fk_orders_shippers
ALTER TABLE IF EXISTS orders ADD CONSTRAINT fk_orders_shippers FOREIGN KEY (ship_via) REFERENCES shippers(shipper_id);

-- fk_order_details_products
ALTER TABLE IF EXISTS order_details ADD CONSTRAINT fk_order_details_products FOREIGN KEY (product_id) REFERENCES products(product_id);

-- fk_order_details_orders
ALTER TABLE IF EXISTS order_details ADD CONSTRAINT fk_order_details_orders FOREIGN KEY (order_id) REFERENCES orders(order_id);

-- fk_products_categories
ALTER TABLE IF EXISTS products ADD CONSTRAINT fk_products_categories FOREIGN KEY (category_id) REFERENCES categories(category_id);

-- fk_products_suppliers
ALTER TABLE IF EXISTS products ADD CONSTRAINT fk_products_suppliers FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id);

-- fk_territories_region
ALTER TABLE IF EXISTS territories ADD CONSTRAINT fk_territories_region FOREIGN KEY (region_id) REFERENCES region(region_id);

-- fk_employee_territories_territories
ALTER TABLE IF EXISTS employee_territories ADD CONSTRAINT fk_employee_territories_territories FOREIGN KEY (territory_id) REFERENCES territories(territory_id);

-- fk_employee_territories_employees
ALTER TABLE IF EXISTS employee_territories ADD CONSTRAINT fk_employee_territories_employees FOREIGN KEY (employee_id) REFERENCES employees(employee_id);

-- fk_customer_customer_demo_customer_demographics
ALTER TABLE IF EXISTS employees ADD CONSTRAINT fk_employees_employees FOREIGN KEY (reports_to) REFERENCES employees(employee_id);