📋 Full Test Case Plan — AutomationExercise.com
Total: 95 Test Cases

🔐 Module 1: User Registration (14 TCs)
#Test CaseTypeTC_001Register new user with all valid details✅ HappyTC_002Register with already existing email❌ NegativeTC_003Register with empty name field❌ NegativeTC_004Register with empty email field❌ NegativeTC_005Register with invalid email format❌ NegativeTC_006Register with mismatched passwords❌ NegativeTC_007Register with all mandatory fields only✅ HappyTC_008Register with special characters in name⚠️ EdgeTC_009Register with very long name (>100 chars)⚠️ EdgeTC_010Verify all account detail fields save correctly✅ HappyTC_011Verify "ACCOUNT CREATED!" message appears✅ HappyTC_012Delete account after registration✅ HappyTC_013Verify "ACCOUNT DELETED!" message appears✅ HappyTC_014Register with spaces-only in name field❌ Negative

🔑 Module 2: Login / Logout (10 TCs)
#Test CaseTypeTC_015Login with valid email and password✅ HappyTC_016Login with incorrect password❌ NegativeTC_017Login with unregistered email❌ NegativeTC_018Login with empty email field❌ NegativeTC_019Login with empty password field❌ NegativeTC_020Login with both fields empty❌ NegativeTC_021Verify "Logged in as username" is visible after login✅ HappyTC_022Logout successfully from logged-in state✅ HappyTC_023Verify user is redirected to login page after logout✅ HappyTC_024Access protected page (cart checkout) without login❌ Negative

🏠 Module 3: Home Page & Navigation (8 TCs)
#Test CaseTypeTC_025Verify home page loads successfully✅ HappyTC_026Verify all navigation menu items are visible✅ HappyTC_027Navigate to Products page from nav menu✅ HappyTC_028Navigate to Cart page from nav menu✅ HappyTC_029Navigate to Signup/Login page from nav menu✅ HappyTC_030Navigate to Test Cases page from nav menu✅ HappyTC_031Verify logo redirects to home page✅ HappyTC_032Scroll to bottom and verify footer is visible✅ Happy

🛍️ Module 4: Products & Search (12 TCs)
#Test CaseTypeTC_033Verify All Products page is visible✅ HappyTC_034Verify products list is displayed✅ HappyTC_035View product detail page of first product✅ HappyTC_036Verify product detail page has name, price, category, availability, condition, brand✅ HappyTC_037Search for a valid product by name✅ HappyTC_038Verify searched products are displayed✅ HappyTC_039Search with empty input❌ NegativeTC_040Search for a product that doesn't exist❌ NegativeTC_041Search with special characters⚠️ EdgeTC_042Filter products by category (Women)✅ HappyTC_043Filter products by category (Men)✅ HappyTC_044Filter products by brand✅ Happy

🛒 Module 5: Cart (12 TCs)
#Test CaseTypeTC_045Add product to cart from product list✅ HappyTC_046Add product to cart from product detail page✅ HappyTC_047Add multiple products to cart✅ HappyTC_048Verify product name, price, and quantity in cart✅ HappyTC_049Increase product quantity from detail page and verify in cart✅ HappyTC_050Remove a product from cart✅ HappyTC_051Remove all products and verify cart is empty⚠️ EdgeTC_052Verify cart persists after navigating away✅ HappyTC_053Verify cart persists after login✅ HappyTC_054Add same product twice and verify quantity updates⚠️ EdgeTC_055Proceed to checkout from cart while logged out❌ NegativeTC_056Verify "Continue Shopping" button works after adding to cart✅ Happy

💳 Module 6: Checkout & Order Placement (12 TCs)
#Test CaseTypeTC_057Place order: Login before checkout✅ HappyTC_058Place order: Register during checkout✅ HappyTC_059Place order: Register before checkout✅ HappyTC_060Verify address details on checkout page✅ HappyTC_061Verify order review on checkout page✅ HappyTC_062Place order with valid payment details✅ HappyTC_063Place order with empty card number❌ NegativeTC_064Place order with empty CVC❌ NegativeTC_065Place order with expired card date❌ NegativeTC_066Verify "Order Placed Successfully" message✅ HappyTC_067Download invoice after successful order✅ HappyTC_068Verify invoice file downloads successfully✅ Happy

📬 Module 7: Contact Us (6 TCs)
#Test CaseTypeTC_069Submit contact form with all valid fields✅ HappyTC_070Verify "GET IN TOUCH" heading is visible✅ HappyTC_071Submit form with file attachment✅ HappyTC_072Submit form with empty name field❌ NegativeTC_073Submit form with empty email field❌ NegativeTC_074Submit form with empty message field❌ Negative

📧 Module 8: Subscription (6 TCs)
#Test CaseTypeTC_075Subscribe with valid email on home page✅ HappyTC_076Subscribe with valid email on cart page✅ HappyTC_077Verify subscription success message✅ HappyTC_078Subscribe with invalid email format❌ NegativeTC_079Subscribe with empty email field❌ NegativeTC_080Subscribe with already subscribed email⚠️ Edge

🔄 Module 9: Scroll & UI Behavior (5 TCs)
#Test CaseTypeTC_081Scroll down to footer and verify it's visible✅ HappyTC_082Scroll up using "Arrow" button and verify top✅ HappyTC_083Scroll up without arrow button and verify top✅ HappyTC_084Verify "Scroll Up" arrow appears after scrolling down✅ HappyTC_085Verify page title/header text on each major page✅ Happy

🧾 Module 10: Account Management (6 TCs)
#Test CaseTypeTC_086View and verify account details after login✅ HappyTC_087Update account information successfully✅ HappyTC_088Update with empty first name❌ NegativeTC_089Update with invalid email format❌ NegativeTC_090Delete account while logged in✅ HappyTC_091Verify deleted account cannot login❌ Negative

🌐 Module 11: Cross-cutting / Misc (4 TCs)
#Test CaseTypeTC_092Verify all pages load without console errors✅ HappyTC_093Verify correct page URLs for all major pages✅ HappyTC_094Verify website is accessible on different screen sizes (responsive)⚠️ EdgeTC_095Verify broken links do not exist on home page⚠️ Edge

📊 Summary
TypeCount✅ Happy Path55❌ Negative30⚠️ Edge/Boundary10Total95