from flask import Flask, render_template, request

app = Flask(__name__)
app.template_folder = 'templates'
app.static_folder = 'static'


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/loadAccount')
def loadAccount():
    acc = "heelosa"
    return render_template('account.html', account=acc)

@app.route('/loadPage01')

def loadPage01():
    lst = [0,1,2,3]
    return render_template("page_01_for.html", seq=lst)


@app.route('/loadPage03')
def loadPage03():
    import pandas as pd
    data = {
        'Name': ['John', 'Anna', 'Peter', 'Linda'],
        'Age':[28,24,35,32],
        'City': ['New York', 'Paris', 'Berlin', 'London']
    }

    df = pd.DataFrame(data)
    html_data = df.to_html(classes='data',escape=False)
    return render_template('page_03_table.html', table = html_data)



if __name__ == '__main__':
    app.run(debug=True)