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
        quantity: 5,
        subject: 'VERBAL_LOGIC',
        questionType: 'READING',
        questionSubType: 'MATCH',
        difficulty: 'MEDIUM',
        topicCategory: "HISTORY"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const response = http.post(
        `${BASE_URL}/question-generations`,
        payload,
        params
    );

    check(response, {
        'status is 201': (r) => r.status === 201,
    });

    sleep(1);
}
