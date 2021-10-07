

#############################################
######### SIMPLE FACTORIAL FUNCTION #########
#############################################

#*

Go-Like inspired type following, a function per defajlt consists
of:
- The function keyword "fn"
- The function identifier = a variable name.
- The function parameters in round brackets: (parameter name and
  its' type)
- The return type
- The function body.

Functions will have two ways of sending output. One of them is
the "exit" keyword, which acts as regular "return" in most
languages.

The "if" keyword usually requires a code body, but it is not
needed if followed by 'methods', in this case, the exit-method.
Example of if block which is followed by setting:
if x == 5 do x := 4

*#

fn factorial (n uint) int {
   if n <= 1
      exit 1
   exit factorial(n - 1) * n
}

##############################
######### CONDITIONS #########
##############################

#*

It's worth nothing that conditional operators are stackable from
left to right.

*#

# LEFT-LOWEST (or equality)
a < b <= c < d       # equivalent to:     a < b && b <= c && c < d
a == b == c          # equivalent to:     a == b && b == c
a < b == c < d       # equivalent to:     a < b && b == c && c < d

# RIGHT LOWEST
a > b > c            # also possible

# ILLEGAL
a < b > c            # but: could technically be interpreted as a < b && b > a
a > b < c            # therefore... could be legal?

#####################################
######### PRINTING, METHODS #########
#####################################

#*

A form of functions provided by ScriptPL are methods. These do
not require round brackets and get a keyword highlight. Unlike
functions, that cannot return a value, they are just a set of
other methods.
Below is how content is printed to the console/ output.

Methods consist of the following structure:

            \/ No comma here
METHOD_TYPE   PARAM1, PARAM2, ...

*#

out factorial(5)

# Create a method consuming any amount of parameters.
# Output all parameters into the "out"-method, in addition the
# 0A-byte: newline character.
# This is how to create a method to output a newline and content
mtd outln (params [...]) {
   out params, b0A
}

mtd with_parameters (i int, j int, k int) {
   # Do something
}

mtd without_parameters () {
   # Do something
}

outln 'Hello, World!'
with_parameters 5, 5, 5
without_parameters

##################################
######### RETURN vs EXIT #########
##################################

#*

As mentioned previously, to quit function, you write "exit".
Alternatively you can "return" a value and keep the function
running in a separate thread.

The function below returns a referenced variable (declaration)
"prime_list", that is an array containing the numbers 2 and 3

*#

fn primes (max uint) []uint {
   return be prime_list := [2, 3]

   for i := (5 to max) += 2 {
      # Generate primes here
   }
}

#############################
######### FOR LOOPS #########
#############################

# General scheme
for (var =) ((start) to (end)) (jump)
for i :=    (start   to end)   (+= / -= / *=) increment

# Examples
for (i := start; i < end; i++)   # Traditional for-loop
for i := start to end            # for (i = start; i <= end; i++)
for start to end                 # ^
for i := element in array        # for .. in
for element in array             # ^

#######################################
######### FIBONACCI GENERATOR #########
#######################################

# This function will run indefinitely.
fn fibonacci () []uint {
   return be f := [0, 1]

   loop {
      f += f[f.l - 1] + f[f.l] # push to f: sum of last two values
   }
}

array.l # returns array length
array.len # too

# Behind them are getters. Functions that are called without brackets.
# More about structure-methods/ functions/ getters later.

# Here's an exmple how to force the end of a function

be x := fibonacci()  # Start a fibonacci collector
sleep 1000           # Sleep main thread for 1000 milliseconds
x.exit               # End x's function thread. This is a method on a
                     # type. More about them, later.
out x                # [0, 1, 1, 2, 3, 5, 8, 13, 21, ... ]

##################################################
######### INLINE FUNCTIONS - MATH SYNTAX #########
##################################################

fn f (x) {
   exit 2 * x + 5
}

out f(2)

# the function is is the same as

fn f (x) -> {
   exit 2 * x + 5
}

# is the same as

fn f (x) -> 2 * x + 5

###########################
######### IMPORTS #########
###########################

use (
   'lib:time'                          # Import as 'time'
   'pkg:github.com/Something/proj'     # Import as 'proj'
   'src:path/to/file.sc'               # Import 'file' from source
   '/path/to/file.sc'                  # Also import from source
   './path/to/file.sc'                 # Import 'file' from current directory
   '../path/to/file.sc'                # Import 'file' from parent directory
   as  <- 'lib/assert'                 # Import 'assert' as 'as'
   p   <- 'pkg/@package-manager/pack'  # Import 'pack' as 'p'
   flt <- '/path/to/filter.sc'         # Import 'filter' from source as 'flt'
)

# special

use *strict        # strict could forbid declaration of operations, some meta programming, etc.
use (*unsafe *asm) # unsafe could support an 'eval' method to execute string as code
                   # asm could allow direct assembly commands

# "strict" forces strict programming:
#   - no further meta programming
#   - no assembly coding (?)
# "unsafe" could open declarations of variables to outside of code
#   - 'eval' method to JIT-compile strings into the language
# "asm": assembly input
#   - assembly coding in code

#######################################
######### REGULAR EXPRESSIONS #########
#######################################

be pattern := R'hello world'gi
out 'Developers love to write "Hello World!" as their first program':find(pattern) # (25, 35) or similar output

be pat1 := r'[\w\d\._%\-]+@[\w\d\._%\-]+\.[\w]{2..64}'gi
be pat2 := r'p{2 | 4..8}q'gi # Matches ppq, ppppq ... ppppppppq (p can be two or four to eight times)

out 'hello@example.com' * pat1 # string * pattern returns boolean: match or not



###########################################
######### ENTITY COMPONENT SYSTEM #########
###########################################

comp EmptyTag # Component that is just a tag

comp Position {
   x int
   y int
}

comp Velocity {
   x int, y int
}

comp Asset {
   image Image
}

be predefinedComp = Velocity {
   x = 5
   y = 5
}

be hero int := new (
   EmptyTag
   Position {
      x = 0
      y = 0
   }
   predefinedComp
   Asset {
      image = new Image
   }
)

del hero 

#
# Iterate over component-combinations
#

for eid int := query (Position, Velocity) {
   Position[eid].x += Velocity[eid].x
   Position[eid].y += Velocity[eid].y
}

for eid int := query (Position, Asset) {
   Asset[eid].image.render(Position[eid].x, Position[eid].y)
}

# In case for loop is not nested and
# entity id is not required. Shortcut:

for query (Position, Velocity) {
   Position.x += Velocity.x
   Position.y += Velocity.y
}

for query (Position, Asset) {
   Asset.image.render(Position.x, Position.y)
}

############################
######### BACK END #########
############################

use ('lib/server')

be app := server()

#app.get('/', (req, res) -> &async {
#   return 
#})

@app.get '/'
@async # In this case technically unused.
(req, res)  -> {
   return 'Hello, World!'
}

@app.get '*'
(req, res) -> {
   return 404
}

# &get '*' -> 404 # Shortcut (?)

# app.serve(':8080')
# attribute to a number
@app.serve 8080

##############################
######### ATTRIBUTES #########
##############################

#   @get '...'   { ... }
attr get (path str) x func<(), int> {
   # VERY ABSTRACT!!! ADD THIS TO THE EVENT HANDLER OR SO
   if request == path {
      x()
   }

   # ALSO POTENTIAL:

   x:$before '*' {
      #*
      Code before the function. Think of :$before like
      CSS attributes.
      
      Declarations to be visible by function content
      with pub keyword.
      
      Possible:

      *
      block.if
      block.for
      op.declaration
      op.set
      mtd.method_name
      attr.attribute_name

      *#
   }

   x:$after 'keyword.return' {
      out 'Returned a variable!'
   }
}

@get 'path/to/site' () -> return 404

###

attr m () x any {
   x:$declare {
      out 'Hello, Apple!'
   }

   x:$set (y) {
      out 'Value set to ', y
   }
}

@m () -> {
   out 'Hello, Banana!'
   # Hello, Apple!  # Attribute Call
}

@m
be x = 5
# Hello, Apple!
# Value is 5

#############################
# Testing Libraries Example #
#############################

# func() is a function with a null return type
# code   is an empty code block
# function inherits from code, hence can also be tested
attr test () -> x code {
   out 'Test Block', b0A, b0A

   attr expect ( message str ) -> b bool {
      out b ? 'PASSED' ~ 'FAILED'
      out ' ', message
   }
}

@test {
   #* ATTRIBUTE *#
   @expect 'true to be true'
   #* THE ATTRIBUTE ENDS HERE *#
   true
}

@test fn () {}

#################################
# M E T A   P R O G R A M I N G #
#################################
#
# a + b + c * d
#         |___|
#           *
# |___|
#   +
# |___________|
#       +
#

$[priority = 10, stackable = true]
op 'c'
(left int, right int) int -> {
   exit left * 2 + right
}

out 5 c 4     # 14
out 5 c 4 c 3 # 14 c 3 = 31 (since stackable)

#################################
# Scalar product of two vectors #
#################################

be x1 := (1, 2, 3) # <int, int, int> = <int; 3>
be x2 := (4, 5, 6)

op 'o' (
   l <int; 3>, r <int; 3>
) int -> {
   exit l.0 * r.0 + l.1 * r.1 + l.2 * r.2
}

out x1 o x2 # 32

##########
# Vector #
##########

struct Vector [int; 3]

v1 Vector := (1, 2, 3)
v2 Vector := (4, 5, 6)

ext Vector {
   $op '+' (self, v Vector) {
      exit self.0 + x.0, self.1 + x.1, self.2 + x.2
   }
}

out v1 + v2 # Vector (5, 7, 9)

############################
######### ASSEMBLY #########
############################

use (*asm)

#
# (f - full, optional), h - half, q - quarter
#

rA-f  # First FULL Register     1111 (4 bytes)
rB-h  # Second HALF Register    0011 (2 bytes)
rC-q  # Third QUARTER Register  0001 (1 byte)
rD    # Fourth Register (full by default)

#
# Assembly Flags
#

fO # Overflow Flag:        High-order bit overflow after arithmetic operation
fD # Direction Flag:
fI # Interrupt Flag:
fT # Trap Flag:
fS # Sign Flag:            Sign of result of arithmetic operation
fZ # Zero Flag:            Result of arihtmetic operation is zero
fA # Auxiliary Carry Flag: 
fP # Parity Flag:          Amount of zeros after arithmetic operation
fC # Carry Flag:           Overflow/ Carry:   1010 + 1011 = 1, 0101

#
# OTHER LANGUAGES BUILT IN
#

# JavaScript Built-In
js (
   function k {
      let k = 'Hello, World!'
      console.log(k)
   }
)

# Lua Built-In (or not)
#*lua (
   function k ()
      local k = "Hello, World!"
      print (k)
   end
)*#

# Html Built-In
</
   <tag attribute="value">
      <b>Hello, {{
         "World!"
      }}!</b>
   </tag>
/>

fn getHtml() {
   exit </<b>Hello, World!</b>/>
}

# Assembly Writing
asm (
   mov %eax, %edx
   add %eax, 2
)

#############################
# ABSOLUTE META PROGRAMMING #
#############################
#
# Meta program parse-time

$$ {

   # You can declare some state variables here, for example,
   # you could make some sort of calculation how much a variable was used.

   $emit (c str) bool {
      # c is the substring: current parse location to end
      # called every parse event
      # return true if claimed

      exit c * r'^start_here'
   }
   
   # Return how many characters you have read.
   $parse (c str) int {
      be x := 0

      loop {
         if c * r'^end_or_so' exit

         x += 1
         c := c.sub(1)

         if c == '' exit
      }

      exit x
   }

   fn parser(content str) []str {
      be tokens []str = []

      loop {
         if content * '^2' {
            tokens.push('2')
            next
         } else if content * '^\+' {
            tokens.push('\+')
            next
         }

         content := content.sub(1)
         if content == '' exit
      }

      exit tokens
   }

   # You can also return a string, and raw Script code.
   $execute (content str) code {
      be tokens := parser(content)
      exit {
         out tokens.filter(e -> e == '2').len
      }
   }
}

start_here
   2 + 2
end_or_so

# outputs 4

# you could write api's with that, for example something like rest.