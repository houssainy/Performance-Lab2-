# Return random number using Multiplicative generator, with avoiding overflows
# using the following formula:
#   random = ax mod m = g(x) + m * h(x)
# Where:
#   g(x) = a * (x mod q) - r * (x div q)
#   h(x) = (x div q) - ((a * x) div m)
# And:
#   q = m div a
#   r = m mod a
#
LCG <- function(a, m, x) {
  q = m %/% a # Integer division
  r = m %% a

  if(r > q) {
    "r is larger than q"
    return(NULL)
  }

  x_div_q = x %/% q
  x_mod_q = x %% q

  gx = (a * x_mod_q) - (r * x_div_q)

  # If gx is negative then hx equal to 1 otherwise 0
  if(gx < 0)
    x_new = gx + m
  else
    x_new = gx

  return(x_new)
}

# TODO(Mohsen) Write function desc
# 
# u: is uniform random variable from 0 to 1
#
exponentialRandomDeviate <- function(lamda, u) {
  return(-1 / lamda * log(u))
}

# Start
# Q1
x = 1
a = 7^5
m = 2^31 - 1
observations = c()
for (i in 1:100000) {
  x = LCG(a, m, x)
  observations = append(observations, x)
}
paste("X100000 = ", x)
hist(observations)

observations = observations / m

"Chi-square Test"
expectedValue = sum(observations) / length(observations)
paste("expectedValue = ", expectedValue)

chiVal = sum((observations - expectedValue)^2 / expectedValue)
paste("chi-val = ", chiVal)

chiThre = qchisq(1 - 0.05, length(observations) - 1, lower.tail=FALSE)
paste("ChiThre of 0.95 = ", chiThre)

if(chiVal < chiThre) "We will Accept the null hypothis that LCG generated values are following Uniform dist." else "We will Reject the null hypothis"

# Q2

# sortedObservations = sort(observations/m)
# z = exponentialRandomDeviate(1, sortedObservations)
# # hist(z)
# x = c (0: (length(z) - 1))
# plot(z, sortedObservations)
# Q3