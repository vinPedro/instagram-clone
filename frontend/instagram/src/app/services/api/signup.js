const API_URL = "http://localhost:8080/api/v1";

export async function createUser(user) {
    const res = await fetch(`${API_URL}/auth/signup`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    });

    const data = await res.json();

    if (!res.ok) {
        throw new Error('Error creating user');
    }
    console.log(data);
    return data;
}
