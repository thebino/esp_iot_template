#include <stdio.h>
#include "unity.h"

using namespace std;

void test_sample_succeeds()
{
   TEST_ASSERT_EQUAL(1, 1);
}

void test_sample_fails()
{
   TEST_ASSERT_EQUAL(1, 1);
}

/**
 * @brief   entry-point on linux systems for host based unit testing
 * 
 * @param   argc
 * @param   argv
 * 
 */
int main(int argc, char **argv)
{
    UNITY_BEGIN();
    RUN_TEST(test_sample_succeeds);
    RUN_TEST(test_sample_fails);
    int failures = UNITY_END();
    return failures;
}

/**
 * @brief   entry-point on an esp device
 * 
 */
void app_main() {
    // not used on host based tests
    printf("This is a host based test and should be started within a linux environment instead!");
}
