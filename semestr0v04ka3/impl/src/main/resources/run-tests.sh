#!/bin/sh

STATS_FILE="/app/stats.txt"

curl -s -o Solution.java "$SOLUTION_URL" || {
    echo "DOWNLOAD FAILED: Solution.java"
    exit 1
}

curl -s -o SolutionTest.java "$SOLUTION_TEST_URL" || {
    echo "DOWNLOAD FAILED: SolutionTest.java"
    exit 1
}

javac -cp junit-platform-console-standalone.jar Solution.java SolutionTest.java 2> compile-errors.txt
if [ $? -ne 0 ]; then
    echo "COMPILATION FAILED"
    cat compile-errors.txt
    exit 1
fi

/usr/bin/time -f "TIME:%e\nMEM:%M" -o "$STATS_FILE" java -jar junit-platform-console-standalone.jar \
    --class-path . \
    --scan-class-path \
    --details tree \
    --reports-dir /app/test-results \
    > test-output.txt 2>&1

TEST_EXIT_CODE=$?

echo "=== TEST OUTPUT ==="
cat test-output.txt

echo "=== METRICS ==="
cat "$STATS_FILE"

exit $TEST_EXIT_CODE
