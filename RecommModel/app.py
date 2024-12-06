from flask import Flask, request, jsonify
import pickle
import pandas as pd

app = Flask(__name__)

# Загрузка модели
with open('diet_recommendation_model.pkl', 'rb') as f:
    model = pickle.load(f)

@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.json
    user_data = pd.DataFrame([data])

    # Предварительная обработка данных пользователя (например, кодирование категориальных признаков)
    user_data = pd.get_dummies(user_data, columns=['activity_level', 'diet_type', 'goal'])
    
    # Обеспечение, что все столбцы модели присутствуют
    for col in model.feature_importances_:
        if col not in user_data.columns:
            user_data[col] = 0

    # Прогнозирование
    recommended_calories = model.predict(user_data)[0]

    return jsonify({'recommended_calories': recommended_calories})

if __name__ == '__main__':
    app.run(debug=True)
