import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 3,
    duration: '30s',

    thresholds: {
        http_req_duration: ['p(95)<60000'],
        http_req_failed: ['rate<0.05'],
    },
};

const BASE_URL = 'http://app:8080';

export default function () {
    const payload = JSON.stringify({
        title: 'title',
        subject: 'VERBAL_LOGIC',
        questionType: 'READING',
        questionSubType: 'MATCH',
        difficulty: 'MEDIUM',
        topicCategory: "HISTORY",
        targetQuestionCount: 2
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MjUwNzQ0NTQxNjM1Mjk3MjgiLCJpYXQiOjE3ODE1MTc2MzgsImV4cCI6MTc4MzMxNzYzOH0.d2qNSqBVxHxwbpAIeiH9t0qIeSWjj9Sz3x8gcddyiSg`,
        },
    };

    const response = http.post(
        `${BASE_URL}/exams`,
        payload,
        params
    );

    console.log(`status: ${response.status}, body: ${response.body}`);

    check(response, {
        'status is 201': (r) => r.status === 201,
    });

    sleep(1);
}
