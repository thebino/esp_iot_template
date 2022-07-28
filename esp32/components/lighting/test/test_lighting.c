#include "unity.h"
#include "lighting.h"

TEST_CASE("turn on successful", "[lighting]")
{
    // given
    int pixel_number = 123;

    // when
    int result = lighting_turn_on(pixel_number);

    // then
    TEST_ASSERT_EQUAL(1, result);
}
